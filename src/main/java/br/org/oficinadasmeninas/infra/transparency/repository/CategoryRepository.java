package br.org.oficinadasmeninas.infra.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.repository.ICategoryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class CategoryRepository implements ICategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Category save(Category category) {
        String query = """
                insert into categories(
                    id, name, is_image, priority
                ) values (?, ?, ?::int, ?)
                """;

        jdbcTemplate.update(
            query,
            category.getId(),
            category.getName(),
            category.getIsImage(),
            category.getPriority()
        );

        return category;
    }

    @Override
    public Category update(Category category) {
        String query = """
                update categories set
                    name = ?,
                    is_image = ?,
                    priority = ?
                where id = ?
                """;

        jdbcTemplate.update(
            query,
            category.getName(),
            category.getIsImage(),
            category.getPriority(),
            category.getId()
        );

        return category;
    }

    @Override
    public void delete(UUID id) {
        String query = "delete from categories where id = ?";

        jdbcTemplate.update(query, id);
    }

    @Override
    public Category findById(UUID id) {
        String query = "select * from categories where id = ?";

        return jdbcTemplate.queryForObject(query, this::mapRow, id);
    }

    @Override
    public List<Category> findAll() {
        String query = "select * from categories order by priority";

        return jdbcTemplate.query(query, this::mapRow);
    }

    private Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Category(
                rs.getObject("id", java.util.UUID.class),
                rs.getString("name"),
                rs.getBoolean("is_image"),
                rs.getInt("priority")
        );
    }
}
