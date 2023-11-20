document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('search-btn').addEventListener('click', function() {
        const startStationId = document.getElementById('start-station-id').value;
        const endStationId = document.getElementById('end-station-id').value;
        fetchGraphData(startStationId, endStationId);
    });


    function fetchGraphData(startStationId, endStationId) {
        const url = new URL('http://localhost:8080/api/get10Shortest');
        
        // Add query parameters
        url.searchParams.append('startId', startStationId);
        url.searchParams.append('endId', endStationId);
    
        // Use the fetch API to get data from the backend
        fetch(url.toString(), {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            const processedData = processGraphData(data);
            renderGraph(processedData);
        })
        .catch(error => {
            console.error('Error fetching graph data:', error.message);
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

    function renderGraph(graph) {
        d3.select('#graph-container').html('');
        const width = window.innerWidth * 0.9;
        const height = window.innerHeight * 0.9;
    
        const svg = d3.select('#graph-container').append('svg')
            .attr('width', width)
            .attr('height', height);
    
        const colorScale = d3.scaleOrdinal(d3.schemeCategory10);
        const uniqueEdges = Array.from(new Set(graphData.map(d => `${d.source}-${d.target}`)));
        uniqueEdges.forEach(edge => colorScale(edge));
    
        const nodePositions = calculateNodePositions(graphData, width, height);
    
        const links = svg.selectAll(".link")
            .data(graphData)
            .enter().append("line")
            .attr("class", "link")
            .style('stroke', d => colorScale(`${d.source}-${d.target}`))
            .style('stroke-width', 2)
            .attr('x1', d => nodePositions[d.source].x)
            .attr('y1', d => nodePositions[d.source].y)
            .attr('x2', d => nodePositions[d.target].x)
            .attr('y2', d => nodePositions[d.target].y);
    
        // Draw nodes
        const nodes = svg.selectAll(".node")
        .data(Object.keys(nodePositions))
        .enter().append("circle")
        .attr("class", "node")
        .attr("r", 5)
        .attr("cx", d => nodePositions[d].x)
        .attr("cy", d => nodePositions[d].y)
        .style("fill", "#69b3a2");


        // Draw node labels
        const labels = svg.selectAll('.label')
            .data(graph.nodes)
            .enter().append('text')
            .attr('class', 'label')
            .attr('x', d => nodePositions[d].x + 10)
            .attr('y', d => nodePositions[d].y + 5)
            .text(d => d)
            .style('fill', '#555')
            .style('font-size', '12px');
    
        // Draw edge weights
        svg.selectAll('.edge-weight')
            .data(graph.links)
            .enter().append('text')
            .attr('x', d => (nodePositions[d.source].x + nodePositions[d.target].x) / 2)
            .attr('y', d => (nodePositions[d.source].y + nodePositions[d.target].y) / 2)
            .text(d => `${d.weight} seconds`)
            .style('fill', '#555')
            .style('font-size', '10px');
    }

    function calculateNodePositions(graphData, width, height) {
        const nodes = Array.from(new Set(graphData.flatMap(d => [d.source, d.target])));
        const angleStep = (2 * Math.PI) / nodes.length;
        const radius = Math.min(width, height) / 2 - 50;
        const nodePositions = {};
    
        nodes.forEach((node, index) => {
            const angle = index * angleStep;
            nodePositions[node] = {
                x: (width / 2) + radius * Math.cos(angle),
                y: (height / 2) + radius * Math.sin(angle)
            };
        });
    
        return nodePositions;
    }
});
