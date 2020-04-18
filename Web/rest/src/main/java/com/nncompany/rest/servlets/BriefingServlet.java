package com.nncompany.rest.servlets;

import com.nncompany.api.interfaces.services.IBriefingService;
import com.nncompany.api.model.entities.Briefing;
import com.nncompany.api.model.wrappers.RequestError;
import com.nncompany.api.model.wrappers.ResponseList;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rest/creds")
public class BriefingServlet {

    @Autowired
    IBriefingService briefingService;

    @ApiOperation(value = "Get briefings with pagination")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Briefings received successfully", response = ResponseList.class),
            @ApiResponse(code = 400, message = "Invalid query params", response = RequestError.class),
            @ApiResponse(code = 404, message = "Can't find briefings", response = RequestError.class)
    })
    @GetMapping("/briefings")
    public ResponseEntity<Object> getAllBriefings(@RequestParam Integer page,
                                                  @RequestParam Integer pageSize){
        if(page < 0 || pageSize < 1) {
            return new ResponseEntity<>(new RequestError(400,
                                                        "query params error",
                                                        "query params must be: page >= 0 and pageSize > 0"),
                                                        HttpStatus.BAD_REQUEST);
        }
        List<Briefing> briefings = briefingService.getWithPagination(page, pageSize);
        if(briefings != null) {
            return ResponseEntity.ok(new ResponseList(briefings, briefingService.getTotalCount()));
        } else {
            return new ResponseEntity<>(new RequestError(404,
                                                        "briefings not found",
                                                        "briefings list = null"),
                                                        HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Get briefing by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Briefing received successfully", response = Briefing.class),
            @ApiResponse(code = 400, message = "Invalid path variable", response = RequestError.class),
            @ApiResponse(code = 404, message = "Can't find briefing", response = RequestError.class)
    })
    @GetMapping("/briefings/{id}")
    public ResponseEntity<Object> getBriefingById(@PathVariable Integer id){
        if(id < 1) {
            return new ResponseEntity<>(new RequestError(400,
                                                        "path variable error",
                                                        "path variable must be > 0"),
                                                        HttpStatus.BAD_REQUEST);
        }
        Briefing briefing = briefingService.get(id);
        if(briefing == null) {
            return new ResponseEntity<>(new RequestError(404,
                    "briefing not found",
                    "briefing deleted or not created"),
                    HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(briefing);
    }

    @ApiOperation(value = "Add new briefing")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Briefing created successfully"),
            @ApiResponse(code = 400, message = "Invalid request body, check models for more info"),
            @ApiResponse(code = 403, message = "Hasn't access, relogin as admin", response = RequestError.class)
    })
    @PostMapping("/briefings")
    public ResponseEntity addBriefing(@RequestBody Briefing briefing){
        briefingService.save(briefing);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @ApiOperation(value = "Update briefing by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Briefing updated successfully"),
            @ApiResponse(code = 400, message = "Invalid request body or URL path variable, check models for more info"),
            @ApiResponse(code = 403, message = "Hasn't access, relogin as admin", response = RequestError.class),
            @ApiResponse(code = 404, message = "Briefing with current id is not found", response = RequestError.class)
    })
    @PutMapping("/briefings/{id}")
    public ResponseEntity updateBriefing(@PathVariable Integer id,
                                         @RequestBody Briefing briefing){
        if(id < 1) {
            return new ResponseEntity<>(new RequestError(400,
                                                        "path variable error",
                                                        "path variable must be > 0"),
                                                        HttpStatus.BAD_REQUEST);
        }
        if(briefingService.get(id) == null) {
            return new ResponseEntity<>(new RequestError(404,
                                                        "briefings not found",
                                                        "briefing with current id is not found"),
                                                        HttpStatus.NOT_FOUND);
        }
        briefing.setId(id);
        briefingService.update(briefing);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Deleter briefing by id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Briefing deleted successfully"),
            @ApiResponse(code = 400, message = "Invalid URL path variable"),
            @ApiResponse(code = 403, message = "Hasn't access, relogin as admin", response = RequestError.class),
            @ApiResponse(code = 404, message = "Briefing with current id is not found", response = RequestError.class)
    })
    @DeleteMapping("/briefings/{id}")
    public ResponseEntity deleteBriefing(@PathVariable Integer id){
        if(id < 1) {
            return new ResponseEntity<>(new RequestError(400,
                                                        "path variable error",
                                                        "path variable must be > 0"),
                                                        HttpStatus.BAD_REQUEST);
        }
        Briefing briefing = briefingService.get(id);
        if(briefing == null) {
            return new ResponseEntity<>(new RequestError(404,
                                                        "briefings not found",
                                                        "briefing with current id is not found"),
                                                        HttpStatus.NOT_FOUND);
        }
        briefingService.delete(briefing);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
