package it.bitrock.bitrockairways.controller;

import it.bitrock.bitrockairways.model.Plane;
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
    public Plane createPlane(@RequestBody Plane plane) {
        return planeService.create(plane);
    }
}
