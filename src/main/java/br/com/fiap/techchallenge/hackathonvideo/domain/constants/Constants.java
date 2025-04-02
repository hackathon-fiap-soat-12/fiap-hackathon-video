package br.com.fiap.techchallenge.hackathonvideo.domain.constants;

public class Constants {
    private static final String PATH_BASE = "/";
    public static final String BUCKET_NAME = "videofiles";
    public static final String PATH_VIDEO = BUCKET_NAME + PATH_BASE + "videos/";
    public static final String PATH_FRAMES = BUCKET_NAME + PATH_BASE + "frames/";

    private Constants() {}
}
