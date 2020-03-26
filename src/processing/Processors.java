package processing;

import data.Data;

/**
 * This static-only class provides several factory methods and classes for creating {@link
 * Processor} objects.
 */
public class Processors {

    // static only class that should not be instantiated; hide constructor
    private Processors() {
    }

    public static Processor scale(double min, double max) {
        return new Scaler() {
            @Override
            public double getMin() {
                return min;
            }

            @Override
            public double getMax() {
                return max;
            }

        };
    }

    public static Processor standardize() {

        return new Processor() {

            @Override
            public Data process(Data data) {

                double dataAvg = DataUtil.avg(data);
                double dataStd = DataUtil.std(data);
                double[] array = new double[data.size()];
                int i = 0;
                for (double val : data) {
                    array[i] = (val - dataAvg) / dataStd;
                    i++;
                }

                return new Data(array);
            }

            @Override
            public String getName() {
                return "Standardizer";
            }
        };
    }

    public static Processor clip(double lower, double upper) {
        return new Clipper(true, true, lower, upper);
    }

    public static Processor clipLower(double lower) {
        return new Clipper(true, false, lower, 0);
    }

    public static Processor clipUpper(double upper) {
        return new Clipper(false, true, 0, upper);
    }


    private static class Clipper implements Processor {
        boolean clipLower;
        boolean clipUpper;
        double lower;
        double upper;

        Clipper(boolean clipLower, boolean clipUpper, double lower, double upper) {
            this.clipLower = clipLower;
            this.clipUpper = clipUpper;
            this.lower = lower;
            this.upper = upper;
        }

        @Override
        public Data process(Data data) {

            int i = 0;
            double[] array = new double[data.size()];

            if (this.clipLower&&this.clipUpper) {
                for (double val : data) {
                    if (val < this.lower) val = this.lower;
                    if (val > this.upper) val = this.upper;
                    array[i] = val;
                    i++;
                }
            }

            else if (this.clipLower) {
                for (double val : data) {
                    if (val < this.lower) val = this.lower;
                    array[i] = val;
                    i++;
                }
            }
            else if (this.clipUpper) {
                for (double val : data) {
                    if (val > this.upper) val = this.upper;
                    array[i] = val;
                    i++;
                }
            }
            else{//safety condition
                for (double val : data) {
                    array[i] = val;
                    i++;
                }
            }

            return new Data(array);
        }

            public String getName(){
                StringBuilder str = new StringBuilder();
                str.append("(");
                if (this.clipLower && this.clipUpper) {
                    str.append("lower = ").append(this.lower).append(",").append("upper = ").append(this.upper);
                } else if (this.clipLower) {
                    str.append("lower = ").append(this.lower);
                } else if (this.clipUpper) {
                    str.append("upper = ").append(this.upper);
                }
                str.append(")");
                return str.toString();
            }
        }

        public abstract static class Scaler implements Processor {

            abstract double getMin();

            abstract double getMax();

            @Override
            public Data process(Data data) {

                double dataMin = DataUtil.min(data);
                double dataMax = DataUtil.max(data);

                int j = 0;
                double[] array = new double[data.size()];//fills new array with scaled data values
                for (double val : data) {
                    double scaled = (val - dataMin) / (dataMax - dataMin);
                    double newVal = scaled * (getMax() - getMin()) + getMin();
                    array[j] = newVal;
                    j++;
                }

                return new Data(array);// returns new Data object with scaled values
            }


            @Override
            public String getName() {
                return "Scaler(" + getMin() + "," + getMax() + ")";
            }


        }


        public static class PercentScaler extends Scaler {
            @Override
            public double getMin() {
                return 0;
            }

            @Override
            public double getMax() {
                return 100;
            }


        }

        // static helper class for statistical measures of Data objects
        private static class DataUtil {

            /**
             * Returns the minimum of the specified <code>Data</code> object.
             */
            public static double min(Data data) {
                double min = Double.POSITIVE_INFINITY;
                for (double d : data) {
                    if (d < min) {
                        min = d;
                    }
                }
                return min;
            }

            /**
             * Returns the maximum of the specified <code>Data</code> object.
             */
            public static double max(Data data) {
                double max = Double.NEGATIVE_INFINITY;
                for (double d : data) {
                    if (d > max) {
                        max = d;
                    }
                }
                return max;
            }

            /**
             * Returns the average (mean) of the specified <code>Data</code> object.
             */
            public static double avg(Data data) {
                double sum = 0;
                for (double d : data) {
                    sum += d;
                }
                return sum / data.size();
            }

            /**
             * Returns the standard deviation of the specified <code>Data</code> object.
             */
            public static double std(Data data) {
                double avg = avg(data);
                double sum = 0;
                for (double d : data) {
                    double deviation = d - avg;
                    sum += deviation * deviation;
                }
                return Math.sqrt(sum / data.size());
            }

        }

    }
