package hello;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by dmitrii on 06/11/15.
 */
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    List<Customer> findByLastNameStartsWithIgnoreCase(String lastName);
}
