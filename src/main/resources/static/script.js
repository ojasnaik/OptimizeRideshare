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
        // Dynamically set width and height based on the screen size
        const width = window.innerWidth * 0.8, height = window.innerHeight * 0.8;

        const svg = d3.select('#graph-container').append('svg')
                      .attr('width', width)
                      .attr('height', height);

        // ... Rest of the existing setup for nodes and links ...
        const nodes = Array.from(new Set(graphData.flatMap(d => [d.source, d.target])),
                            id => ({ id }));

        // Create the simulation
        const simulation = d3.forceSimulation(nodes)
        .force("link", d3.forceLink(graphData).id(d => d.id))
        .force("charge", d3.forceManyBody())
        .force("center", d3.forceCenter(width / 2, height / 2));

        // Create labels for the nodes
        const labels = svg.append("g")
                          .attr("class", "labels")
                          .selectAll("text")
                          .data(nodes)
                          .enter().append("text")
                          .attr("dx", 12)
                          .attr("dy", ".35em")
                          .text(d => d.id)
                          .style("fill", "#555")
                          .style("font-size", "12px");

        // Create labels for the links (edges)
        const edgepaths = svg.append("g")
                             .selectAll(".edgepath")
                             .data(graphData)
                             .enter().append("path")
                             .attr("class", "edgepath")
                             .style("fill-opacity", 0)
                             .style("stroke-opacity", 0)
                             .style("pointer-events", "none");

        const edgelabels = svg.append("g")
                              .selectAll(".edgelabel")
                              .data(graphData)
                              .enter().append("text")
                              .style("pointer-events", "none")
                              .attr("class", "edgelabel")
                              .attr("id", (d, i) => 'edgelabel' + i)
                              .style("fill", "#aaa")
                              .style("font-size", "10px");

        edgelabels.append("textPath")
                  .attr("xlink:href", (d, i) => '#edgepath' + i)
                  .style("text-anchor", "middle")
                  .style("pointer-events", "none")
                  .attr("startOffset", "50%")
                  .text(d => d.weight + ' seconds');

        // Update positions after each simulation tick
        simulation.on("tick", () => {
            
            link.attr("x1", d => d.source.x)
            .attr("y1", d => d.source.y)
            .attr("x2", d => d.target.x)
            .attr("y2", d => d.target.y);

            labels.attr("x", d => d.x)
                  .attr("y", d => d.y);

            edgepaths.attr('d', d => {
                return 'M ' + d.source.x + ' ' + d.source.y + ' L ' + d.target.x + ' ' + d.target.y;
            });

            edgelabels.attr('transform', d => {
                return 'translate(' + (d.source.x + d.target.x) / 2 + ',' + (d.source.y + d.target.y) / 2 + ')';
            });
        });
    }
    
});
