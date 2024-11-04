package hello.aiofirst.service;

import hello.aiofirst.config.KakaoPayProperties;
import hello.aiofirst.domain.*;
import hello.aiofirst.dto.KakaoCancelResponseDTO;
import hello.aiofirst.repository.PaymentRepository;
import hello.aiofirst.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoPayService {

    private final KakaoPayProperties kakaoPayProperties;
    private final PaymentRepository paymentRepository;
    private final PointRepository pointRepository;

    private HttpHeaders getHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        String auth = "SECRET_KEY " + kakaoPayProperties.getSecretKey();
        log.info("auth={}",auth);
        httpHeaders.set("Authorization",auth);
        httpHeaders.set("Content-Type","application/json");
        return httpHeaders;
    }

    @Transactional
    public void kakaoCancel(Order order){

        Payment payment = paymentRepository.findByOrderId(order.getId());

        HttpEntity<Map<String,String >> requestEntity = new HttpEntity<>(getCancelParameters(payment), this.getHeaders());

        RestTemplate restTemplate = new RestTemplate();

        KakaoCancelResponseDTO kakaoCancelResponseDTO =
                restTemplate.postForObject("https://open-api.kakaopay.com/online/v1/payment/cancel",requestEntity, KakaoCancelResponseDTO.class);

        log.info("kakaoCancelResponseDTO ={}",kakaoCancelResponseDTO);
        payment.changeStauts(PaymentStatus.CANCEL);

        Point point = pointRepository.getPoint(order.getId());
        
        if (point.getPointStatus().equals(PointStatus.PENDING) || point.getPointStatus().equals(PointStatus.EARNED)){
            point.pendingChange(PointStatus.EARNED_CANCEL);
        }

    }

    private Map<String, String> getCancelParameters(Payment payment) {
        Map<String,String > parameters = new HashMap<>();
        parameters.put("cid", kakaoPayProperties.getCid());
        parameters.put("tid", payment.getTid());
        parameters.put("cancel_amount", String.valueOf(payment.getTotal_amount()));
        parameters.put("cancel_tax_free_amount", String.valueOf(payment.getTax_free_amount()));
        return parameters;
    }




}
