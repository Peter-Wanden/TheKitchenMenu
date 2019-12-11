package com.example.peter.thekitchenmenu.ui.utils;

import android.content.res.Resources;
import android.text.Html;

import com.example.peter.thekitchenmenu.R;

import java.util.Objects;

import javax.annotation.Nonnull;

public class TextValidator {

    public enum RequestType {
        SHORT_TEXT,
        LONG_TEXT
    }

    public enum Result {
        INVALID_REQUEST,
        TOO_SHORT,
        TOO_LONG,
        VALID
    }

    private int shortTextMinLength;
    private int shortTextMaxLength;
    private int longTextMinLength;
    private int longTextMaxLength;

    public TextValidator(Resources resources) {
        shortTextMinLength = resources.getInteger(R.integer.input_validation_short_text_min_length);
        shortTextMaxLength = resources.getInteger(R.integer.input_validation_short_text_max_length);
        longTextMinLength = resources.getInteger(R.integer.input_validation_long_text_min_length);
        longTextMaxLength = resources.getInteger(R.integer.input_validation_long_text_max_length);
    }

    public Response validateText(Request request) {

        switch (request.getRequest()) {
            case SHORT_TEXT:
                return validateShort(request);
            case LONG_TEXT:
                return validateLong(request);
            default:
                return new Response(
                        Result.INVALID_REQUEST, "", "", "");
        }
    }

    private Response validateShort(Request request) {
        if (request.getTextToValidate().isEmpty() && shortTextMinLength > 0) {
            return getShortTextResponseModel(Result.TOO_SHORT, request.getTextToValidate());

        } else {
            String text = stripOutHtml(request.getTextToValidate());
            if (request.getTextToValidate().length() < shortTextMinLength) {
                return getShortTextResponseModel(Result.TOO_SHORT, text);

            } else {
                if (request.getTextToValidate().length() > shortTextMaxLength) {
                    return getShortTextResponseModel(Result.TOO_LONG, text);

                } else {
                    return getShortTextResponseModel(Result.VALID, text);
                }
            }
        }
    }

    private Response getShortTextResponseModel(Result result, String text) {
        return new Response(
                result,
                text,
                String.valueOf(shortTextMinLength),
                String.valueOf(shortTextMaxLength));
    }

    private Response validateLong(Request request) {
        if (request.getTextToValidate().isEmpty() && longTextMinLength > 0) {
            return getLongTextResponseModel(Result.TOO_SHORT, request.getTextToValidate());

        } else {
            String text = stripOutHtml(request.getTextToValidate());
            if (request.getTextToValidate().length() < longTextMinLength) {
                return getLongTextResponseModel(Result.TOO_SHORT, text);

            } else {
                if (request.getTextToValidate().length() > longTextMaxLength) {
                    return getLongTextResponseModel(Result.TOO_LONG, text);

                } else {
                    return getLongTextResponseModel(Result.VALID, text);
                }
            }
        }
    }

    private Response getLongTextResponseModel(Result result, String text) {
        return new Response(
                result,
                text,
                String.valueOf(longTextMinLength),
                String.valueOf(longTextMaxLength));
    }

    private String stripOutHtml(String inputText) {
        return Html.fromHtml(inputText, Html.FROM_HTML_MODE_LEGACY).toString();
    }

    public static final class Request {
        @Nonnull
        private final RequestType requestType;
        @Nonnull
        private final String textToValidate;

        public Request(@Nonnull RequestType requestType, @Nonnull String textToValidate) {
            this.requestType = requestType;
            this.textToValidate = textToValidate;
        }

        @Nonnull
        public RequestType getRequest() {
            return requestType;
        }

        @Nonnull
        public String getTextToValidate() {
            return textToValidate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Request that = (Request) o;
            return requestType == that.requestType && textToValidate.equals(that.textToValidate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(requestType, textToValidate);
        }

        @Override
        public String toString() {
            return "RequestModel{" +
                    "requestType=" + requestType +
                    ", textToValidate='" + textToValidate + '\'' +
                    '}';
        }
    }

    public static final class Response {
        @Nonnull
        private final Result result;
        @Nonnull
        private final String text;
        @Nonnull
        private final String minLength;
        @Nonnull
        private final String maxLength;

        public Response(@Nonnull Result result,
                        @Nonnull String text,
                        @Nonnull String minLength,
                        @Nonnull String maxLength) {
            this.result = result;
            this.text = text;
            this.minLength = minLength;
            this.maxLength = maxLength;
        }

        @Nonnull
        public Result getResult() {
            return result;
        }

        @Nonnull
        public String getText() {
            return text;
        }

        @Nonnull
        public String getMinLength() {
            return minLength;
        }

        @Nonnull
        public String getMaxLength() {
            return maxLength;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "result=" + result +
                    ", text='" + text + '\'' +
                    ", minLength='" + minLength + '\'' +
                    ", maxLength='" + maxLength + '\'' +
                    '}';
        }
    }
}
