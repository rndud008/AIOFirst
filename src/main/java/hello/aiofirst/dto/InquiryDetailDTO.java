package hello.aiofirst.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryDetailDTO {
    private Long inquiryId;
    private Long categoryId;

    private String categoryName;
    private String username;
    private String content;
    private String title;
    private String createdAt;

    private boolean inquiryAnswer;

    private Long InquiryAnswerId;
    private String adminUsername;
    private String adminContent;
    private String adminCreatedAt;
}
