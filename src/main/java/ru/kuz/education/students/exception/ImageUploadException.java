package ru.kuz.education.students.exception;

public class ImageUploadException extends RuntimeException {

    public ImageUploadException(
            final String message
    ) {
        super(message);
    }

}
