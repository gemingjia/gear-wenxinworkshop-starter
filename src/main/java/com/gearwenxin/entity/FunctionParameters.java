package com.gearwenxin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ge Mingjia
 * {@code @date} 2023/10/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunctionParameters {
    private String name;
    private String description;
    private Map<String, Map<String, String>> properties;
}
