package com.cht.network.monitoring.domain;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "topology")
@IdClass(TopologyPK.class)
public class Topology {

    @Id
    private String id;

    private String name;

    private String configs;

    private String nodes;

    private String edges;

    private String layouts;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name="background_image")
    private String backgroundImage;

    @Column(name="background_image_width")
    private String backgroundImageWidth;

    @Column(name="background_image_height")
    private String backgroundImageHeight;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfigs() {
        return configs;
    }

    public void setConfigs(String configs) {
        this.configs = configs;
    }

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public String getEdges() {
        return edges;
    }

    public void setEdges(String edges) {
        this.edges = edges;
    }

    public String getLayouts() {
        return layouts;
    }

    public void setLayouts(String layouts) {
        this.layouts = layouts;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getBackgroundImageWidth() {
        return backgroundImageWidth;
    }

    public void setBackgroundImageWidth(String backgroundImageWidth) {
        this.backgroundImageWidth = backgroundImageWidth;
    }

    public String getBackgroundImageHeight() {
        return backgroundImageHeight;
    }

    public void setBackgroundImageHeight(String backgroundImageHeight) {
        this.backgroundImageHeight = backgroundImageHeight;
    }
}
