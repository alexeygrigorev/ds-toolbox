package com.alexeygrigorev.dstools.opt;

import com.alexeygrigorev.dstools.models.Model;

public interface ParameterOptimizer<M extends Model> {

    BestParams findBestParams();

    public static interface OptimizationCallback<M extends Model> {
        double execute(M model);
    }

    public static class BestParams {
        private final Params params;
        private final double bestValue;

        public BestParams(Params params, double bestValue) {
            this.params = params;
            this.bestValue = bestValue;
        }

        public double getBestValue() {
            return bestValue;
        }

        public Params getParams() {
            return params;
        }
    }

}
