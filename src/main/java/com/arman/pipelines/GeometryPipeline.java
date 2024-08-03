package com.arman.pipelines;

public class GeometryPipeline implements Pipeline {

    private Pipeline[] pipelines;

    public GeometryPipeline() {
        this.pipelines = new Pipeline[]{new LightingPipeline(), new ProjectionPipeline(), new ClippingPipeline()};
    }

    @Override
    public void execute() {
        for (int i = 0; i < this.pipelines.length; i++) {
            this.pipelines[i].execute();
        }
    }

}
