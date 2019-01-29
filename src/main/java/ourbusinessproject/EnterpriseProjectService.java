package ourbusinessproject;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class EnterpriseProjectService {

    @PersistenceContext
    private EntityManager entityManager;

    public Project saveProjectForEnterprise(Project project, Enterprise enterprise) {
        Enterprise newEnterprise = saveEnterprise(enterprise);
        newEnterprise = entityManager.merge(newEnterprise);
        project.setEnterprise(newEnterprise);
        Project newProject = entityManager.merge(project);
        newEnterprise.addProject(newProject);
        /* Trouver l'entreprise qui a ce project si il est déjà a une
         * entreprise et retirer a cette entreprise le project
         */
        
        Project oldProject  = findProjectById(newProject.getId());
        if (oldProject != null) {
        	Enterprise oldEnterprise = findEnterpriseById(oldProject.getId());
        	if (oldEnterprise != null) {
        		Collection<Project> listOldProjectForOldEnterprise = oldEnterprise.getProjects();
                if (listOldProjectForOldEnterprise.contains(oldProject)) {
                	listOldProjectForOldEnterprise.remove(oldProject);
                	oldEnterprise.setProjects(listOldProjectForOldEnterprise);
                	saveEnterprise(oldEnterprise);
                }
        	}
        }
        
        saveEnterprise(newEnterprise);
        entityManager.persist(newProject);
        entityManager.flush();
        return newProject;
    }

    public Enterprise saveEnterprise(Enterprise enterprise) {
    	Enterprise managedEnterprise = entityManager.merge(enterprise);
        entityManager.persist(managedEnterprise);
        entityManager.flush();
        return managedEnterprise;
    }

    public Project findProjectById(Long id) {
        return entityManager.find(Project.class, id);
    }

    public Enterprise findEnterpriseById(Long id) {
        return entityManager.find(Enterprise.class, id);
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Project> findAllProjects() {
        TypedQuery<Project> query = entityManager.createQuery("select p from Project p join fetch p.enterprise order by p.title", Project.class);
        return query.getResultList();
    }
}
