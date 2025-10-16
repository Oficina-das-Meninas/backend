package br.org.oficinadasmeninas.infra.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.Collaborator;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCollaboratorDto;
import br.org.oficinadasmeninas.domain.transparency.repository.ICategoriesRepository;
import br.org.oficinadasmeninas.domain.transparency.repository.ICollaboratorsRepository;
import br.org.oficinadasmeninas.infra.transparency.repository.queries.CollaboratorsQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CollaboratorsRepository implements ICollaboratorsRepository {

    private final JdbcTemplate jdbc;
    private final ICategoriesRepository categoriesRepository;

    @Autowired
    public CollaboratorsRepository(JdbcTemplate jdbc, ICategoriesRepository categoriesRepository) {
        this.jdbc = jdbc;
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public UUID insert(CreateCollaboratorDto request) {
        var id = UUID.randomUUID();

        jdbc.update(CollaboratorsQueryBuilder.INSERT_COLLABORATOR,
                id,
                request.image(),
                request.categoryId(),
                request.name(),
                request.role(),
                request.description(),
                request.priority() != null ? request.priority() : 0
        );

        return id;
    }

    @Override
    public void delete(UUID id) {
        jdbc.update(CollaboratorsQueryBuilder.DELETE_COLLABORATOR, id);
    }


    @Override
    public int countByCategoryId(UUID id) {
        Integer count = jdbc.queryForObject(CollaboratorsQueryBuilder.COUNT_COLLABORATORS_BY_CATEGORY, Integer.class, id);
        return count == null ? 0 : count;
    }

    @Override
    public Optional<Collaborator> findById(UUID id) {
        try {
            var collaborator = jdbc.queryForObject(CollaboratorsQueryBuilder.GET_COLLABORATOR_BY_ID, this::mapRowCollaborator, id);
            return Optional.ofNullable(collaborator);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Collaborator> findAll() {
        return jdbc.query(CollaboratorsQueryBuilder.GET_ALL_COLLABORATORS, this::mapRowCollaborator);
    }

    private Collaborator mapRowCollaborator(ResultSet rs, int rowNum) throws SQLException {
        Collaborator collaborator = new Collaborator();
        collaborator.setId(rs.getObject("id", UUID.class));
        collaborator.setImage(rs.getString("preview_image_url"));
        collaborator.setName(rs.getString("name"));
        collaborator.setRole(rs.getString("role"));
        collaborator.setDescription(rs.getString("description"));
        collaborator.setPriority(rs.getInt("priority"));

        UUID categoryId = rs.getObject("category_id", UUID.class);

        Category category = categoriesRepository.findById(categoryId)
                .orElse(null);
        collaborator.setCategory(category);

        return collaborator;
    }
}
