package com.shineidle.tripf.chatbot;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class TrainDoccatModel {
    public static void main(String[] args) {
        try {
            // 학습 데이터 파일 불러오기
            File trainingFile = new ClassPathResource("models/train.txt").getFile();
            InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(trainingFile);

            // 학습 데이터를 OpenNLP가 이해할 수 있도록 변환
            ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, "UTF-8");
            ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

            // 훈련 파라미터 설정
            TrainingParameters trainingParameters = TrainingParameters.defaultParams();
            trainingParameters.put(TrainingParameters.ITERATIONS_PARAM, "100");
            trainingParameters.put(TrainingParameters.CUTOFF_PARAM, "1");

            // 모델 학습
            DoccatModel model = DocumentCategorizerME.train("ko", sampleStream, trainingParameters, new DoccatFactory());

            // 학습된 모델 저장
            File modelFile = new File("src/main/resources/models/doccat.bin");
            try (OutputStream modelOut = new FileOutputStream(modelFile)) {
                model.serialize(modelOut);
            }

            log.info("OpenNLP 문서 분류 모델이 생성되었습니다: {}", modelFile.getAbsolutePath());

        } catch (Exception e) {
            log.info("모델 학습 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}
