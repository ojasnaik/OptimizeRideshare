document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('search-btn').addEventListener('click', function() {
        const startStationId = document.getElementById('start-station-id').value;
        const endStationId = document.getElementById('end-station-id').value;
        fetchGraphData(startStationId, endStationId);
    });


function fetchGraphData(startStationId, endStationId) {
    const url = `http://localhost:8080/api/get10Shortest?startId=${startStationId}&endId=${endStationId}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            renderGraph(processGraphData(data));
        })
        .catch(error => {
            console.error('Error fetching graph data:', error);
        });
}
    function processGraphData(data) {
    // Flatten the array of arrays into a single array
        const flattenedData = data.flat();

        // Process the data as required for D3
        // (This part depends on how you want to use the 'weight' in your visualization)
        const graphData = flattenedData.map(item => ({
            source: item.source,
            target: item.target,
            weight: item.weight
            }));

         return graphData;
    }


    function renderGraph(graphData) {
        const width = 800, height = 600;
    
        const svg = d3.select('#graph-container').append('svg')
                          .attr('width', width)
                          .attr('height', height);
    
        // Extract nodes from graphData
        const nodes = Array.from(new Set(graphData.flatMap(d => [d.source, d.target])),
                                id => ({ id }));
    
        // Create the simulation
        const simulation = d3.forceSimulation(nodes)
            .force("link", d3.forceLink(graphData).id(d => d.id))
            .force("charge", d3.forceManyBody())
            .force("center", d3.forceCenter(width / 2, height / 2));
    
        // Create lines for the links
        const link = svg.append("g")
                        .selectAll("line")
                        .data(graphData)
                        .enter().append("line")
                        .style("stroke", "#aaa");
    
        // Create circles for the nodes
        const node = svg.append("g")
                        .selectAll("circle")
                        .data(nodes)
                        .enter().append("circle")
                        .attr("r", 5)
                        .style("fill", "#69b3a2");
    
        // Update positions after each simulation tick
        simulation.on("tick", () => {
            link.attr("x1", d => d.source.x)
                .attr("y1", d => d.source.y)
                .attr("x2", d => d.target.x)
                .attr("y2", d => d.target.y);
    
            node.attr("cx", d => d.x)
                .attr("cy", d => d.y);
        });
    }
    
});
