package com.intecon.documentparser.service.impl;

import java.awt.image.*;
  
  
public class NoiseFilter  {
      
      
    public final static int IMPULSE = 0;
      
    public final static int GAUSSIAN = 1;
      
    protected int noiseType = IMPULSE;
      
    protected double stdDev = 10.0;
      
    protected double impulseRatio = 0.05;
      
      
    public NoiseFilter() {
    }
      
      
    public NoiseFilter(int noiseType) {
        setNoiseType(noiseType);
    }
      
      
    public NoiseFilter(int noiseType, double parameter) {
        setNoiseType(noiseType);
          
        if (noiseType == IMPULSE) setImpulseRatio(parameter);
        if (noiseType == GAUSSIAN) setGaussianStdDev(parameter);
    }
      
      
    public void setNoiseType(int noiseType) {
        this.noiseType = noiseType;
    }
      
      
    public int getNoiseType() {
        return noiseType;
    }
      
      
    public void setGaussianStdDev(double stdDev) {
        this.stdDev = stdDev;
    }
      
      
    public double getGaussianStdDev() {
        return stdDev;
    }
      
    public void setImpulseRatio(double impulseRatio) {
        this.impulseRatio = impulseRatio;
    }
      
    public double getImpulseRatio() {
        return impulseRatio;
    }
      
    public java.awt.image.BufferedImage filter(BufferedImage image, BufferedImage output) {
        output = verifyOutput(output, image);
          
//        switch (noiseType) {
//            default:
//            case IMPULSE: return impulseNoise(image, output);
//            case GAUSSIAN: return gaussianNoise(image, output);
//        }
        return impulseNoise(image, output);
    }
      
      
    protected BufferedImage impulseNoise(BufferedImage image, BufferedImage output) {
        output.setData(image.getData());
          
        Raster source = image.getRaster();
        WritableRaster out = output.getRaster();
          
        double rand;
        double halfImpulseRatio = impulseRatio / 2.0;
        int bands  = out.getNumBands();
        int width  = image.getWidth();  // width of the image
        int height = image.getHeight(); // height of the image
        java.util.Random randGen = new java.util.Random();
          
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                rand = randGen.nextDouble();
                if (rand < halfImpulseRatio) {
                    for (int b=0; b<bands; b++) out.setSample(i, j, b, 0);
                } else if (rand < impulseRatio) {
                    for (int b=0; b<bands; b++) out.setSample(i, j, b, 255);
                }
            }
        }
          
        return output;
    }
      
      
    protected BufferedImage gaussianNoise(BufferedImage image, BufferedImage output) {
        Raster source = image.getRaster();
        WritableRaster out = output.getRaster();
          
        int currVal;                    // the current value
        double newVal;                  // the new "noisy" value
        double gaussian;                // gaussian number
        int bands  = out.getNumBands(); // number of bands
        int width  = image.getWidth();  // width of the image
        int height = image.getHeight(); // height of the image
        java.util.Random randGen = new java.util.Random();
          
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                gaussian = randGen.nextGaussian();
                  
                for (int b=0; b<bands; b++) {
                    newVal = stdDev * gaussian;
                    currVal = source.getSample(i, j, b);
                    newVal = newVal + currVal;
                    if (newVal < 0)   newVal = 0.0;
                    if (newVal > 255) newVal = 255.0;
                      
                    out.setSample(i, j, b, (int)(newVal));
                }
            }
        }
          
        return output;
    }
    public BufferedImage verifyOutput(BufferedImage output, BufferedImage input) {
        return verifyOutput(output, input.getWidth(), input.getHeight(), input.getType());
    }
     
     
    public BufferedImage verifyOutput(BufferedImage output, BufferedImage input, int type) {
        return verifyOutput(output, input.getWidth(), input.getHeight(), type);
    }
     
    public BufferedImage verifyOutput(BufferedImage output, int width, int height, int type) {
         
        if (output != null && output.getWidth() == width &&
                output.getHeight() == height && output.getType() == type) return output;
         
        return new BufferedImage(width, height, type);
    }
}

