package it.bitrock.bitrockairways.service;

import it.bitrock.bitrockairways.exceptions.NoRecordException;
import it.bitrock.bitrockairways.model.Employee;
import it.bitrock.bitrockairways.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository er;
    public EmployeeService(EmployeeRepository er){
        this.er = er;
    }

    public List<Employee> getAllEmployee() throws NoRecordException{
        List<Employee> le = (List<Employee>) er.findAll();
        if(le.isEmpty()){
            throw new NoRecordException("The list of employees is empty");
        }
        return le;
    }

    public List<Employee> getAllBoardMembers() throws NoRecordException{
        List<Employee> le = er.findBoardOfDirectors();
        if(le.isEmpty()){
            throw new NoRecordException("The list of Board of Directors is empty");
        }
        return le;
    }

    public Employee findById(Long id) throws NoRecordException {
        if(er.existsById(id)) {
            Employee e = er.findById(id).get();
            return e;
        }
        throw new NoRecordException("No employee with id " + id + " has been found");
    }

    public List<Employee> findByRole(String role){
        List<Employee> le = er.findByRole(role);
        return le;
    }

    public List<Employee> findByPartialNameSurname(String name, String surname) throws NoRecordException{
        List<Employee> le = er.findByNameSurnameContaining(name, surname);
        if(le.isEmpty()){
            throw new NoRecordException("No result has been found");
        }
        return le;
    }

    public List<Employee> findByPartialName(String name) throws NoRecordException{
        List<Employee> le = er.findByNameContaining(name);
        if(le.isEmpty()){
            throw new NoRecordException("No name has been found who contains '"+name+"'");
        }
        return le;
    }

    public List<Employee> findByPartialSurname(String surname) throws NoRecordException{
        List<Employee> le = er.findBySurnameContaining(surname);
        if(le.isEmpty()){
            throw new NoRecordException("No surname has been found who contains '"+surname+"'");
        }
        return le;
    }
}
