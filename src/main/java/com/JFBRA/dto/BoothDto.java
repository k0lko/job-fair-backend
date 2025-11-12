package com.JFBRA.dto;

import com.JFBRA.model.Booth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoothDto {

    private Long id;
    private String boothNumber;
    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;
    private Integer price;
    private String size;
    private String status;
    private String company;

    public static BoothDto fromEntity(Booth booth) {
        if (booth == null) {
            return null;
        }

        return BoothDto.builder()
                .id(booth.getId())
                .boothNumber(booth.getBoothNumber())
                .x(booth.getX())
                .y(booth.getY())
                .width(booth.getWidth())
                .height(booth.getHeight())
                .price(booth.getPrice())
                .size(booth.getSize() != null ? booth.getSize().getDisplayName() : null)
                .status(booth.getStatus() != null ? booth.getStatus().name() : null)
                .company(booth.getCompany())
                .build();
    }

    public static List<BoothDto> fromEntities(List<Booth> booths) {
        return booths.stream()
                .map(BoothDto::fromEntity)
                .toList();
    }
}
