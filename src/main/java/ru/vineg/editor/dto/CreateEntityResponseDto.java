package ru.vineg.editor.dto;


public class CreateEntityResponseDto {
    private String createdId;

    public CreateEntityResponseDto() {}

    public CreateEntityResponseDto(String createdId) {
        this.createdId = createdId;
    }

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId;
    }
}
