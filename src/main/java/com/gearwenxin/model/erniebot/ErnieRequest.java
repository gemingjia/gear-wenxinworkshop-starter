package com.gearwenxin.model.erniebot;

import com.gearwenxin.annotations.Between;
import com.gearwenxin.annotations.Only;
import com.gearwenxin.model.BaseRequest;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author Ge Mingjia
 * @date 2023/7/20
 * <p>
 * ErnieBot 模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErnieRequest extends BaseRequest {

    /**
     * （1）较高的数值会使输出更加随机，而较低的数值会使其更加集中和确定
     * （2）默认0.95，范围 (0, 1.0]，不能为0
     * （3）建议该参数和top_p只设置1个
     * （4）建议top_p和temperature不要同时更改
     */
    @Only(value = 0)
    @Between(min = 0, max = 1.0, includeMax = true)
    @SerializedName("temperature")
    private Float temperature;

    /**
     * （1）影响输出文本的多样性，取值越大，生成文本的多样性越强
     * （2）默认0.8，取值范围 [0, 1.0]
     * （3）建议该参数和temperature只设置1个
     * （4）建议top_p和temperature不要同时更改
     */
    @Only(value = 0)
    @Between(min = 0, max = 1.0, includeMin = true, includeMax = true)
    @SerializedName("top_p")
    private Float topP;

    /**
     * 通过对已生成的token增加惩罚，减少重复生成的现象。说明：
     * （1）值越大表示惩罚越大
     * （2）默认1.0，取值范围：[1.0, 2.0]
     */
    @Between(min = 1.0, max = 2.0, includeMin = true, includeMax = true)
    @SerializedName("penalty_score")
    private Float penaltyScore;

}