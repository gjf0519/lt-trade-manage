package similar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaijf
 * @description
 * @date 2020/5/14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Similarity {
    private String code;
    private double similarityRido;
    private int similaritySize;
}
