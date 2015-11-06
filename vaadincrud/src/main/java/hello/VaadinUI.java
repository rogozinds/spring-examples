package hello;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

@SpringUI
@Theme("valo")
public class VaadinUI extends UI {

    CustomerRepository repo;
    Grid grid;

    @Autowired
    public VaadinUI(CustomerRepository repo) {
        this.repo = repo;
        this.grid = new Grid();
        grid.setContainerDataSource(
                new BeanItemContainer(Customer.class, repo.findAll()));
    }

    @Override
    protected void init(VaadinRequest request) {
        TextField filter = new TextField();
        filter.setInputPrompt("Filter by last name");
        filter.addTextChangeListener(e -> listCustomers(e.getText()));
        VerticalLayout mainLayout = new VerticalLayout(filter, grid);
        setContent(mainLayout);
    }



    private void listCustomers(String text) {
        if (StringUtils.isEmpty(text)) {
            grid.setContainerDataSource(
                    new BeanItemContainer(Customer.class, repo.findAll()));
        }
        else {
            grid.setContainerDataSource(new BeanItemContainer(Customer.class,
                    repo.findByLastNameStartsWithIgnoreCase(text)));
        }
    }
}