package it.bitrock.bitrockairways.controller;

import it.bitrock.bitrockairways.model.Plane;
import it.bitrock.bitrockairways.model.dto.PlaneCreateDTO;
import it.bitrock.bitrockairways.model.dto.PlaneUpdateDTO;
import it.bitrock.bitrockairways.model.mapper.PlaneMapper;
import it.bitrock.bitrockairways.service.PlaneService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PlaneController {
    private final PlaneService planeService;

    public PlaneController(PlaneService planeService) {
        this.planeService = planeService;
    }

    @PostMapping("/planes")
    @ResponseStatus(HttpStatus.CREATED)
    public Plane createPlane(@RequestBody PlaneCreateDTO planeCreateDTO) {
        Plane plane = PlaneMapper.INSTANCE.planeCreateDtoToPlane(planeCreateDTO);
        return planeService.create(plane);
    }

    @PutMapping("/planes")
    @ResponseStatus(HttpStatus.OK)
    public Plane updatePlane(@RequestBody PlaneUpdateDTO planeUpdateDTO) {
        Plane plane = PlaneMapper.INSTANCE.planeUpdateDtoToPlane(planeUpdateDTO);
        return planeService.update(plane);
    }
}
