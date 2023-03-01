package it.bitrock.bitrockairways.controller;


import it.bitrock.bitrockairways.exceptions.NoRecordException;
import it.bitrock.bitrockairways.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService es;


    @GetMapping("find_by_role/{role}")
    public ResponseEntity getEmployeeByRole(@PathVariable("role") String role){
        return  ResponseEntity.ok(es.findByRole(role));
    }


    @GetMapping("find_by_id/{id}")
    public ResponseEntity getEmployeeById(@PathVariable("id") Long id) throws NoRecordException {
        return  ResponseEntity.ok(es.findById(id));
    }


    @GetMapping("/list_of_employees")
    public ResponseEntity getAllEmployees() throws NoRecordException{
        return ResponseEntity.ok(es.getAllEmployee());
    }


    @GetMapping("/list_of_boards")
    public ResponseEntity getAllBoardMembers() throws NoRecordException{
        return ResponseEntity.ok(es.getAllBoardMembers());
    }


    @GetMapping("/search/{name}/{surname}")
    public ResponseEntity<?> getAllEmployeeByPartialNameSurname(
            @PathVariable("name") String name, @PathVariable("surname") String surname) throws NoRecordException{
        return ResponseEntity.ok(es.findByPartialNameSurname(name,surname));
    }


    @GetMapping("/find_by_name/{name}")
    public ResponseEntity<?> getAllEmployeeByPartialName(
            @PathVariable("name") String name) throws NoRecordException{
        return ResponseEntity.ok(es.findByPartialName(name));
    }


    @GetMapping("/find_by_surname/{surname}")
    public ResponseEntity<?> getAllEmployeeByPartialSurname(
            @PathVariable("surname") String surname) throws NoRecordException{
        return ResponseEntity.ok(es.findByPartialSurname(surname));
    }

}
