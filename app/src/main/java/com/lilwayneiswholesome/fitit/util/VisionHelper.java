package com.lilwayneiswholesome.fitit.util;


import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VisionHelper {
    private static final String CLOUD_VISION_API_KEY = "";

    private static Vision VISION;
    public static void init() {
        Vision.Builder builder = new Vision.Builder(new NetHttpTransport(), new AndroidJsonFactory(), null);
        builder.setVisionRequestInitializer(new VisionRequestInitializer(CLOUD_VISION_API_KEY));
        VISION = builder.build();
    }

    public static String doVision(byte[] imageBytes) throws IOException {
        Image image = new Image();
        image.encodeContent(imageBytes);

        Feature feature = new Feature();
        feature.setType("LABEL_DETECTION");

        AnnotateImageRequest imageRequest = new AnnotateImageRequest();
        imageRequest.setImage(image);
        imageRequest.setFeatures(Arrays.asList(feature));

        List<AnnotateImageRequest> requests = new ArrayList<>();
        requests.add(imageRequest);

        BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();
        batchRequest.setRequests(requests);

        BatchAnnotateImagesResponse response = VISION.images().annotate(batchRequest).execute();
        return response.getResponses().get(0).getLabelAnnotations().get(0).getDescription();
    }
}
