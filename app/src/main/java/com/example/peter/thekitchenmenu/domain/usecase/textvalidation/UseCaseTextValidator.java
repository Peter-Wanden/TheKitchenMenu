package com.example.peter.thekitchenmenu.domain.usecase.textvalidation;

import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;

import java.util.Objects;

import javax.annotation.Nonnull;

public class UseCaseTextValidator
        extends UseCaseInteractor<UseCaseTextValidator.Request, UseCaseTextValidator.Response> {

    private static final String TAG = "tkm-" + UseCaseTextValidator.class.getSimpleName() + ":";

    public enum RequestType {
        SHORT_TEXT,
        LONG_TEXT
    }

    public enum Result {
        TOO_SHORT,
        TOO_LONG,
        VALID
    }

    private final int defaultShortTextMinLength;
    private final int defaultShortTextMaxLength;
    private final int defaultLongTextMinLength;
    private final int defaultLongTextMaxLength;

    private UseCaseTextValidator(int defaultShortTextMinLength,
                                int defaultShortTextMaxLength,
                                int defaultLongTextMinLength,
                                int defaultLongTextMaxLength) {
        this.defaultShortTextMinLength = defaultShortTextMinLength;
        this.defaultShortTextMaxLength = defaultShortTextMaxLength;
        this.defaultLongTextMinLength = defaultLongTextMinLength;
        this.defaultLongTextMaxLength = defaultLongTextMaxLength;
    }

    @Override
    protected void execute(Request request) {
        if (request.getType() == RequestType.SHORT_TEXT) {
            validateShortText(request);
        } else if (request.getType() == RequestType.LONG_TEXT) {
            validateLongText(request);
        } else {
            throw new UnsupportedOperationException("Unknown requestType");
        }
    }

    private void validateShortText(Request request) {
        String text = request.getModel().getText().trim();
        if (text.length() < defaultShortTextMinLength) {
            sendTextTooShortResponse(request);
        } else if (text.length() > defaultShortTextMaxLength) {
            sendTextTooLongResponse(request);
        } else {
            sendTextValidatedResponse(request);
        }
    }

    private void validateLongText(Request request) {
        String text = request.getModel().getText().trim();
        if (text.length() < defaultLongTextMinLength) {
            sendTextTooShortResponse(request);
        } else if (text.length() > defaultLongTextMaxLength) {
            sendTextTooLongResponse(request);
        } else {
            sendTextValidatedResponse(request);
        }
    }

    private void sendTextTooShortResponse(Request request) {
        Response.Builder responseBuilder = new Response.Builder().
                setResult(Result.TOO_SHORT).
                setModel(request.getModel());
        responseBuilder = addRequiredLengths(request, responseBuilder);

        getUseCaseCallback().onError(responseBuilder.build());
    }

    private void sendTextTooLongResponse(Request request) {
        Response.Builder responseBuilder = new Response.Builder().
                setResult(Result.TOO_LONG).
                setModel(request.getModel());
        responseBuilder = addRequiredLengths(request, responseBuilder);

        getUseCaseCallback().onError(responseBuilder.build());
    }

    private void sendTextValidatedResponse(Request request) {
        Response.Builder responseBuilder = new Response.Builder().
                setResult(Result.VALID).
                setModel(request.getModel());
        responseBuilder = addRequiredLengths(request, responseBuilder);

        getUseCaseCallback().onSuccess(responseBuilder.build());
    }

    private Response.Builder addRequiredLengths(Request request, Response.Builder responseBuilder) {
        if (request.getType() == RequestType.SHORT_TEXT) {
            responseBuilder = addShortTextLengths(responseBuilder);

        } else if (request.getType() == RequestType.LONG_TEXT) {
            responseBuilder = addLongTextLengths(responseBuilder);
        }
        return responseBuilder;
    }

    private Response.Builder addShortTextLengths(Response.Builder responseBuilder) {
        return responseBuilder.
                setMinLength(String.valueOf(defaultShortTextMinLength)).
                setMaxLength(String.valueOf(defaultShortTextMaxLength));
    }

    private Response.Builder addLongTextLengths(Response.Builder responseBuilder) {
        return responseBuilder.
                setMinLength(String.valueOf(defaultLongTextMinLength)).
                setMaxLength(String.valueOf(defaultLongTextMaxLength));
    }

    public static class Builder {
        private int shortTextMinLength;
        private int shortTextMaxLength;
        private int longTextMinLength;
        private int longTextMaxLength;

        public Builder setShortTextMinLength(int shortTextMinLength) {
            this.shortTextMinLength = shortTextMinLength;
            return this;
        }

        public Builder setShortTextMaxLength(int shortTextMaxLength) {
            this.shortTextMaxLength = shortTextMaxLength;
            return this;
        }

        public Builder setLongTextMinLength(int longTextMinLength) {
            this.longTextMinLength = longTextMinLength;
            return this;
        }

        public Builder setLongTextMaxLength(int longTextMaxLength) {
            this.longTextMaxLength = longTextMaxLength;
            return this;
        }

        public UseCaseTextValidator build() {
            return new UseCaseTextValidator(
                    shortTextMinLength,
                    shortTextMaxLength,
                    longTextMinLength,
                    longTextMaxLength
            );
        }
    }

    public static final class Model {

        @Nonnull
        private final String text;

        public Model(@Nonnull String text) {
            this.text = text;
        }

        @Nonnull
        public String getText() {
            return text;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return text.equals(model.text);
        }

        @Override
        public int hashCode() {
            return Objects.hash(text);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "text='" + text + '\'' +
                    '}';
        }
    }

    public static final class Request implements UseCaseInteractor.Request {
        @Nonnull
        private final RequestType type;
        @Nonnull
        private final Model model;

        public Request(@Nonnull RequestType type, @Nonnull Model model) {
            this.type = type;
            this.model = model;
        }

        @Nonnull
        public RequestType getType() {
            return type;
        }

        @Nonnull
        public Model getModel() {
            return model;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Request request = (Request) o;
            return type == request.type &&
                    model.equals(request.model);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, model);
        }

        @Override
        public String toString() {
            return "Request{" +
                    "type=" + type +
                    ", model=" + model +
                    '}';
        }
    }

    public static final class Response implements UseCaseInteractor.Response {
        @Nonnull
        private final Result result;
        @Nonnull
        private final Model model;
        @Nonnull
        private String minLength;
        @Nonnull
        private String maxLength;

        public Response(@Nonnull Result result,
                        @Nonnull Model model,
                        @Nonnull String minLength,
                        @Nonnull String maxLength) {
            this.result = result;
            this.model = model;
            this.minLength = minLength;
            this.maxLength = maxLength;
        }

        @Nonnull
        public Result getResult() {
            return result;
        }

        @Nonnull
        public Model getModel() {
            return model;
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
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Response response = (Response) o;
            return result == response.result &&
                    model.equals(response.model) &&
                    minLength.equals(response.minLength) &&
                    maxLength.equals(response.maxLength);
        }

        @Override
        public int hashCode() {
            return Objects.hash(result, model, minLength, maxLength);
        }

        @Override
        public String toString() {
            return "Response{" +
                    "result=" + result +
                    ", model=" + model +
                    ", minLength='" + minLength + '\'' +
                    ", maxLength='" + maxLength + '\'' +
                    '}';
        }

        public static class Builder {
            private Result result;
            private Model model;
            String minLength;
            String maxLength;

            public Builder setResult(Result result) {
                this.result = result;
                return this;
            }

            public Builder setModel(Model model) {
                this.model = model;
                return this;
            }

            public Builder setMinLength(String minLength) {
                this.minLength = minLength;
                return this;
            }

            public Builder setMaxLength(String maxLength) {
                this.maxLength = maxLength;
                return this;
            }

            public Response build() {
                return new Response(
                        result,
                        model,
                        minLength,
                        maxLength
                );
            }
        }
    }
}
