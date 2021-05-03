package net.alainpham.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RenderRegionRequest implements Serializable{

    public String id;
    public String renderId;
    public String requesterHostname;
    public String workerHostname;
    public String status;

    public Date start;
    
    public Date end;

    public Long duration; 

    public String outputPrefix;
    public Integer resolutionX;
    public Integer resolutionY;
    public Integer samples;
    public Integer frameDivider;

    public Integer areaX;
    public Integer areaY;
    public Integer totalTiles;
    public String renderedFilePath;

}
