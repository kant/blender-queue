package net.alainpham.model;

import java.io.Serializable;

public class DistributedFrameRenderRequest implements Serializable{

    public String fileName;
    public Integer resolutionX;
    public Integer resolutionY;
    public Integer samples;
    public Integer frameDivider;
    
}
