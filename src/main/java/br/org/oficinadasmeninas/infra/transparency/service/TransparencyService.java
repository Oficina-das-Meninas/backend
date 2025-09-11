package br.org.oficinadasmeninas.infra.transparency.service;

import br.org.oficinadasmeninas.domain.objectStorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentDto;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.ResponseCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.UpdateCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.exception.EntityNotFoundException;
import br.org.oficinadasmeninas.domain.transparency.mapper.CategoryMapper;
import br.org.oficinadasmeninas.domain.transparency.repository.ITransparencyRepository;
import br.org.oficinadasmeninas.domain.transparency.service.ITransparencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TransparencyService implements ITransparencyService {

    private final IObjectStorage objectStorage;
    private final ITransparencyRepository transparencyRepository;

    @Autowired
    public TransparencyService(IObjectStorage objectStorage, ITransparencyRepository transparencyRepository) {
        this.objectStorage = objectStorage;
        this.transparencyRepository = transparencyRepository;
    }

    @Override
    public ResponseCategoryDto insertCategory(CreateCategoryDto request) {

        Category entity = transparencyRepository.insertCategory(CategoryMapper.toEntity(request));

        return CategoryMapper.toDto(entity);
    }

    @Override
    public ResponseCategoryDto updateCategory(UUID id, UpdateCategoryDto request) {

        Category existing = transparencyRepository.findCategoryById(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + id));

        existing.setName(request.name());
        existing.setPriority(request.priority());

        Category updated = transparencyRepository.updateCategory(existing);

        return CategoryMapper.toDto(updated);
    }

    @Override
    public void deleteCategory(UUID id) {
        checkCategoryExists(id);
        checkCategoryLinks(id);

        transparencyRepository.deleteCategory(id);
    }


    @Override
    public ResponseCategoryDto findCategoryById(UUID id) {

        var category = transparencyRepository.findCategoryById(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + id));

        return CategoryMapper.toDto(category);
    }

    @Override
    public List<ResponseCategoryDto> findAllCategories() {

        return transparencyRepository.findAllCategories().stream()
            .map(CategoryMapper::toDto)
            .toList();
    }

    @Override
    public void uploadDocument(
        MultipartFile file,
        String title,
        Date effectiveDate,
        String categoryId
    ) throws IOException {

        var category = transparencyRepository
                .findCategoryById(UUID.fromString(categoryId))
                .orElseThrow();

        var previewLink = objectStorage.uploadTransparencyFile(file, false);

        var dto = new CreateDocumentDto(title, category.getId(), effectiveDate, previewLink);

        transparencyRepository.insertDocument(dto);
    }

    private void checkCategoryExists(UUID id) {
    if (!transparencyRepository.existsCategoryById(id)) {
        throw new EntityNotFoundException("Categoria não encontrada: " + id);
    }
}

    private void checkCategoryLinks(UUID id) {
        int docs = transparencyRepository.countDocumentsByCategoryId(id);
        int collabs = transparencyRepository.countCollaboratorsByCategoryId(id);

        if (docs > 0 || collabs > 0) {
            throw new IllegalStateException(buildLinkMessage(docs, collabs));
        }
    }

    private String buildLinkMessage(int docs, int collabs) {
        StringBuilder msg = new StringBuilder("Não é possível excluir a categoria porque existem vínculos:");
        if (docs > 0) msg.append(" ").append(docs).append(" documento(s)");
        if (collabs > 0) msg.append(docs > 0 ? " e " : " ").append(collabs).append(" colaborador(es)");
        return msg.toString();
    }
}