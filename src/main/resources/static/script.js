document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('search-btn').addEventListener('click', function() {
        const startStationId = document.getElementById('start-station-id').value;
        const endStationId = document.getElementById('end-station-id').value;
        fetchGraphData('get10ShortestPaths', startStationId, endStationId, false);
    });
    document.getElementById('search-btn-overall').addEventListener('click', function() {
            const startStationId = document.getElementById('start-station-id').value;
            const endStationId = document.getElementById('end-station-id').value;
            fetchGraphData('getOverallCheapestPath', startStationId, endStationId, true);
        });
    document.getElementById('search-btn-dijkstra').addEventListener('click', function() {
                const startStationId = document.getElementById('start-station-id').value;
                const endStationId = document.getElementById('end-station-id').value;
                fetchGraphData('getDijkstraShortestPath', startStationId, endStationId, true);
            });
});
    function fetchGraphData(apiEndpoint, startStationId, endStationId, calculateWeight) {
        const url = `http://localhost:8080/api/${apiEndpoint}?sourceStationId=${startStationId}&destinationStationId=${endStationId}`;

        fetch(url)
            .then(response => response.json())
            .then(data => {
                if (data && Array.isArray(data) && data.some(Array.isArray)) {
                    renderGraph(processGraphData(data), startStationId, endStationId, calculateWeight);
                } else {
                    // Handle unexpected data structure
                    console.error('Unexpected data structure:', data);
                }
            })
            .catch(error => {
                console.error('Error fetching graph data:', error);
            });
    }


    function processGraphData(data) {
        const flattenedData = data.flat();
        return flattenedData.map(item => ({
            source: item.source,
            target: item.target,
            weight: item.weight
        }));
    }

    function renderGraph(graphData, startStationId, endStationId, calculateWeight) {
        d3.select('#graph-container').selectAll('svg').remove();

        d3.select('#graph-container').html('');

        const width = 860;
        const height = 600;
        const radius = Math.min(width, height) / 2 - 40; // Radius of the circle

        const svg = d3.select('#graph-container').append('svg')
                        .attr('width', width)
                        .attr('height', height)
                      .append('g')
                        .attr('transform', `translate(${width / 2}, ${height / 2})`);

        // Define arrow markers for graph links
        svg.append('defs').append('marker')
            .attr('id', 'arrowhead')
            .attr('viewBox', '-0 -5 10 10')
            .attr('refX', 20) // Controls the shift of the arrowhead along the path
            .attr('refY', 0)
            .attr('orient', 'auto')
            .attr('markerWidth', 6) // Smaller width
            .attr('markerHeight', 6) // Smaller height
            .attr('xoverflow', 'visible')
          .append('svg:path')
            .attr('d', 'M 0,-5 L 10 ,0 L 0,5')
            .attr('fill', '#999');

        // Setting up the nodes for the graph
        const nodes = Array.from(new Set(graphData.flatMap(d => [d.source, d.target])),
                                id => ({id}));

        // Calculating angular position for each node in a circle
        const angle = (2 * Math.PI) / nodes.length;
        nodes.forEach((node, index) => {
            node.x = radius * Math.cos(angle * index);
            node.y = radius * Math.sin(angle * index);
        });

        // Draw links
        const link = svg.append("g")
            .attr("stroke", "#999")
            .selectAll("line")
            .data(graphData)
            .join("line")
            .attr("x1", d => nodes.find(node => node.id === d.source).x)
            .attr("y1", d => nodes.find(node => node.id === d.source).y)
            .attr("x2", d => nodes.find(node => node.id === d.target).x)
            .attr("y2", d => nodes.find(node => node.id === d.target).y)
            .attr("stroke-width", 2)
            .attr('marker-end', 'url(#arrowhead)'); // Use arrowhead


            const edgeLabels = svg.append("g")
                .selectAll("text")
                .data(graphData)
                .enter()
                .append("text")
                .attr("x", d => (nodes.find(node => node.id === d.source).x + nodes.find(node => node.id === d.target).x) / 2)
                .attr("y", d => (nodes.find(node => node.id === d.source).y + nodes.find(node => node.id === d.target).y) / 2)
                .text(d => `${d.weight} seconds`)
                .style("font-size", "10px")
                .attr("fill", "black")
                .style("pointer-events", "none") // Make sure the text is not blocking pointer events
                .attr("text-anchor", "middle") // Center the text on the path
                .attr("alignment-baseline", "middle");
        // Node labels
        const labels = svg.append("g")
            .attr("class", "labels")
            .selectAll("text")
            .data(nodes)
            .join("text")
            .attr("x", d => d.x)
            .attr("y", d => d.y)
            .attr("dy", "0.35em") // Vertically centers the text
            .attr("dx", 12) // Positions the text to the right of the node
            .text(d => d.id)
            .style("font-size", "12px")
            .attr("fill", "black")
            .style("pointer-events", "none");

        function colorNode(d) {
            return d.id === startStationId || d.id === endStationId ? "red" : "blue";
        }
        const node = svg.append("g")
                .selectAll("circle")
                .data(nodes)
                .join("circle")
                .attr("r", 5)
                .attr("fill", d => colorNode(d, startStationId, endStationId)) // Pass ids to colorNode
                .attr("cx", d => d.x)
                .attr("cy", d => d.y);

        if(calculateWeight){
            const totalAdjustedWeight = calculateTotalAdjustedWeight(graphData);
            document.getElementById('total-adjusted-weight').textContent = totalAdjustedWeight;
           }
    }

function calculateTotalAdjustedWeight(graphData) {
    let totalWeight = 0;
    graphData.forEach(edge => {
        let adjustedWeight = Math.max(0, edge.weight - 100);
        totalWeight += adjustedWeight;
    });
    return ((totalWeight/60) * 2).toFixed(2);
}