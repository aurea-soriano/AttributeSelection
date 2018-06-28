/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.pointsmatrix;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class DissimilarityFactory {

    public enum DissimilarityType {
        /*CITY_BLOCK("City-block"),
         COSINE_BASED("Cosine-based dissimilarity"),
         EUCLIDEAN("Euclidean"),
         EUCLIDEANSQUARED("EuclideanSquared"),
         EXTENDED_JACCARD("Extended Jaccard"),
         INFINITY_NORM("Infinity norm"),
         DYNAMIC_TIME_WARPING("Dynamic Time Warping (DTW)"),
         MAX_MOVING_EUCLIDEAN("Max Moving Euclidean"),
         MIN_MOVING_EUCLIDEAN("Min Moving Euclidean"),
         SCALINGTIMEWARPING("Scaling and Time Warping (SWM)"),
         UNIFORMSCALING("Uniform Scaling"),
         PEARSONCORRELATION("Pearson Correlation"),
         SPEARMANCORRELATION("Spearman Correlation");     
         */

        EUCLIDEAN("Euclidean");

        private DissimilarityType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        private final String name;
    }

    public static AbstractDissimilarity getInstance(DissimilarityType type) {

        /*if (type.equals(DissimilarityType.CITY_BLOCK)) {
         return new CityBlock();
         } else if (type.equals(DissimilarityType.COSINE_BASED)) {
         return new CosineBased();
         } else*/
        if (type.equals(DissimilarityType.EUCLIDEAN)) {
            return new EuclideanSimilarity();
        }
        /*else if (type.equals(DissimilarityType.EXTENDED_JACCARD)) {
         return new ExtendedJaccard();
         } else if (type.equals(DissimilarityType.INFINITY_NORM)) {
         return new InfinityNorm();
         } else if (type.equals(DissimilarityType.DYNAMIC_TIME_WARPING)) {
         return new DynamicTimeWarping();
         } else if (type.equals(DissimilarityType.MAX_MOVING_EUCLIDEAN)) {
         return new MaxMovingEuclidean();
         } else if (type.equals(DissimilarityType.MIN_MOVING_EUCLIDEAN)) {
         return new MinMovingEuclidean();
         }else if (type.equals(DissimilarityType.EUCLIDEANSQUARED)) {
         return new EuclideanSquared();
         }   else if (type.equals(DissimilarityType.UNIFORMSCALING)) {
         return new UniformScaling();
         } else if (type.equals(DissimilarityType.SCALINGTIMEWARPING)) {
         return new ScalingTimeWarping();
         } else if (type.equals(DissimilarityType.PEARSONCORRELATION)) {
         return new PearsonCorrelation();
         } else if (type.equals(DissimilarityType.SPEARMANCORRELATION)) {
         return new SpearmanCorrelation();
         } 
         */
        return null;
    }
}
