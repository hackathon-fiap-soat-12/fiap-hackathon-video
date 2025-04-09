package br.com.fiap.techchallenge.hackathonvideo.domain.models;

public class Metadata {
    private final String videoName;
    private Integer qtdFrames;
    private Long sizeInBytes;

    public Metadata(String videoName) {
        this.videoName = videoName;
        this.qtdFrames = 0;
        this.sizeInBytes = 0L;
    }

    public Metadata(String videoName, Integer qtdFrames, Long sizeInBytes) {
        this.videoName = videoName;
        this.qtdFrames = qtdFrames;
        this.sizeInBytes = sizeInBytes;
    }

    public void updateMetadata(Integer qtdFrames, Long sizeInBytes) {
        this.qtdFrames = qtdFrames;
        this.sizeInBytes = sizeInBytes;
    }

    public String getVideoName() {
        return videoName;
    }

    public Integer getQtdFrames() {
        return qtdFrames;
    }

    public Long getSizeInBytes() {
        return sizeInBytes;
    }
}
