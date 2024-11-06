package hello.aiofirst.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryDTO {
    private Long inquiryId;
    private Long categoryId;

    private String username;
    private String title;
    private String categoryName;
    private String createdAt;
    private boolean inquiryAnswer;
}
