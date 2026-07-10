/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package som.mod.cfg.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

/**
 *
 * @author Sergio Flores
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SJsonFieldSettings {

    @JsonProperty("words_min")
    private int wordsMin;

    @JsonProperty("words_max")
    private int wordsMax;

    @JsonProperty("words_req")
    private int wordsReq;

    // --- word_1 group ---

    @JsonProperty("word_1_len_min")
    private int word1LenMin;

    @JsonProperty("word_1_len_min_req")
    private int word1LenMinReq;

    @JsonProperty("word_1_len_max")
    private int word1LenMax;

    @JsonProperty("word_1_len_max_req")
    private int word1LenMaxReq;

    // --- word_2 group (optional, defaults to -1 when absent) ---

    private static final int WORD2_ABSENT = -1;

    @JsonProperty("word_2_len_min")
    @JsonSetter(nulls = Nulls.SKIP)
    private int word2LenMin = WORD2_ABSENT;

    @JsonProperty("word_2_len_min_req")
    @JsonSetter(nulls = Nulls.SKIP)
    private int word2LenMinReq = WORD2_ABSENT;

    @JsonProperty("word_2_len_max")
    @JsonSetter(nulls = Nulls.SKIP)
    private int word2LenMax = WORD2_ABSENT;

    @JsonProperty("word_2_len_max_req")
    @JsonSetter(nulls = Nulls.SKIP)
    private int word2LenMaxReq = WORD2_ABSENT;

    // -------------------------
    // Getters
    // -------------------------

    public int getWordsMin()       { return wordsMin; }
    public int getWordsMax()       { return wordsMax; }
    public int getWordsReq()       { return wordsReq; }

    public int getWord1LenMin()    { return word1LenMin; }
    public int getWord1LenMinReq() { return word1LenMinReq; }
    public int getWord1LenMax()    { return word1LenMax; }
    public int getWord1LenMaxReq() { return word1LenMaxReq; }

    public int getWord2LenMin()    { return word2LenMin; }
    public int getWord2LenMinReq() { return word2LenMinReq; }
    public int getWord2LenMax()    { return word2LenMax; }
    public int getWord2LenMaxReq() { return word2LenMaxReq; }

    public boolean hasWord2Group() {
        return word2LenMin != WORD2_ABSENT || word2LenMinReq != WORD2_ABSENT
            || word2LenMax != WORD2_ABSENT || word2LenMaxReq != WORD2_ABSENT;
    }

    @Override
    public String toString() {
        return "WordConfig{" +
            "wordsMin=" + wordsMin +
            ", wordsMax=" + wordsMax +
            ", wordsReq=" + wordsReq +
            ", word1LenMin=" + word1LenMin +
            ", word1LenMinReq=" + word1LenMinReq +
            ", word1LenMax=" + word1LenMax +
            ", word1LenMaxReq=" + word1LenMaxReq +
            ", word2LenMin=" + (word2LenMin == WORD2_ABSENT ? "absent" : word2LenMin) +
            ", word2LenMinReq=" + (word2LenMinReq == WORD2_ABSENT ? "absent" : word2LenMinReq) +
            ", word2LenMax=" + (word2LenMax == WORD2_ABSENT ? "absent" : word2LenMax) +
            ", word2LenMaxReq=" + (word2LenMaxReq == WORD2_ABSENT ? "absent" : word2LenMaxReq) +
            '}';
    }
}
