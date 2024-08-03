package com.arman.pipelines;

public class GraphicsPipeline implements Pipeline {

    private Pipeline[] pipelines;

    public GraphicsPipeline() {
        this.pipelines = new Pipeline[]{new ApplicationPipeline(), new GeometryPipeline(), new RasterizationPipeline()};
    }

    public void execute() {
        for (int i = 0; i < this.pipelines.length; i++) {
            this.pipelines[i].execute();
        }
    }

}
