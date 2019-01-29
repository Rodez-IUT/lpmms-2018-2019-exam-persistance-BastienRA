package ourbusinessproject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1)
    private String title;

    private String description;

    @Version
    private long version;

	@NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Enterprise enterprise;

    public Project() {this.version = 0;}

    public Project(String title, String description) {
        this.title = title;
        this.description = description;
        this.version = 0;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }
    
    public long getVersion() {
		return version;
	}
}
