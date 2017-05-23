package com.ifelsecoders.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageForTranslation {
    @SerializedName("input_lang")
    private String inputLang;

    @SerializedName("output_lang")
    private String outputLang;

    @SerializedName("text")
    private String text;
}
