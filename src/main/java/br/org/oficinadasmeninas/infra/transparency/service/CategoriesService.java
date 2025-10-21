package br.org.oficinadasmeninas.infra.transparency.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.shared.exception.EntityNotFoundException;
import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.Collaborator;
import br.org.oficinadasmeninas.domain.transparency.Document;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCategoryRequestDto;
import br.org.oficinadasmeninas.domain.transparency.dto.ResponseCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.UpdateCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.CategoryResponseDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.CollaboratorResponseDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.DocumentResponseDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.GetCategoriesResponseDto;
import br.org.oficinadasmeninas.domain.transparency.mapper.CategoryMapper;
import br.org.oficinadasmeninas.domain.transparency.mapper.CollaboratorMapper;
import br.org.oficinadasmeninas.domain.transparency.mapper.DocumentMapper;
import br.org.oficinadasmeninas.domain.transparency.repository.ICategoriesRepository;
import br.org.oficinadasmeninas.domain.transparency.repository.ICollaboratorsRepository;
import br.org.oficinadasmeninas.domain.transparency.repository.IDocumentsRepository;
import br.org.oficinadasmeninas.domain.transparency.service.ICategoriesService;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import br.org.oficinadasmeninas.presentation.exceptions.ValidationException;

@Service
public class CategoriesService implements ICategoriesService {

	private final ICategoriesRepository categoriesRepository;
	private final IDocumentsRepository documentsRepository;
	private final ICollaboratorsRepository collaboratorsRepository;

	public CategoriesService(ICategoriesRepository categoriesRepository, IDocumentsRepository documentsRepository,
			ICollaboratorsRepository collaboratorsRepository) {
		this.categoriesRepository = categoriesRepository;
		this.documentsRepository = documentsRepository;
		this.collaboratorsRepository = collaboratorsRepository;
	}

	@Override
	public UUID insertCategory(CreateCategoryRequestDto request) {

		var entity = categoriesRepository.insertCategory(CategoryMapper.toEntity(request));

		return entity.getId();
	}

	@Override
	public UUID updateCategory(UUID id, UpdateCategoryDto request) {

		var existing = categoriesRepository.findCategoryById(id)
				.orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + id));

		existing.setName(request.name());
		existing.setPriority(request.priority());

		var updated = categoriesRepository.updateCategory(existing);

		return updated.getId();
	}

	@Override
	public UUID deleteCategory(UUID id) {
		checkCategoryExists(id);
		checkCategoryLinks(id);

		categoriesRepository.deleteCategory(id);

		return id;
	}

	@Override
	public ResponseCategoryDto getCategoryById(UUID id) {

		var category = categoriesRepository.findCategoryById(id)
				.orElseThrow(() -> new NotFoundException(Messages.CATEGORY_NOT_FOUND));

		return CategoryMapper.toDto(category);
	}

	@Override
	public List<ResponseCategoryDto> getAllCategories() {

		return categoriesRepository.findAllCategories().stream().map(CategoryMapper::toDto).toList();
	}

	@Override
	public GetCategoriesResponseDto getAllCategoriesWithDocuments() {

		var categories = categoriesRepository.findAllCategories();
		var documents = documentsRepository.findAllDocuments();
		var collaborators = collaboratorsRepository.findAllCollaborators();

		return new GetCategoriesResponseDto(mapCategoriesToDto(categories, documents, collaborators));
	}

	private void checkCategoryExists(UUID id) {
		if (!categoriesRepository.existsCategoryById(id)) {
			throw new NotFoundException(Messages.CATEGORY_NOT_FOUND);
		}
	}

	private void checkCategoryLinks(UUID id) {
		int docs = documentsRepository.countDocumentsByCategoryId(id);
		int collabs = collaboratorsRepository.countCollaboratorsByCategoryId(id);

		if (docs > 0 || collabs > 0) {
			throw new ValidationException(buildLinkMessage(docs, collabs));
		}
	}

	private List<CategoryResponseDto> mapCategoriesToDto(List<Category> categories, List<Document> documents,
			List<Collaborator> collaborators) {

		var documentsByCategory = documents.stream().collect(Collectors.groupingBy(d -> d.getCategory().getId()));

		var collaboratorsByCategory = collaborators.stream()
				.collect(Collectors.groupingBy(c -> c.getCategory().getId()));

		return categories.stream()
				.map(cat -> new CategoryResponseDto(cat.getId(), cat.getName(), cat.getImage(), cat.getPriority(),
						Optional.ofNullable(documentsByCategory.get(cat.getId())).map(this::MapDocumentsToDto),
						Optional.ofNullable(collaboratorsByCategory.get(cat.getId())).map(this::MapCollaboratorsToDto)))
				.sorted(Comparator.comparing(CategoryResponseDto::priority)).toList();
	}

	private List<DocumentResponseDto> MapDocumentsToDto(List<Document> documents) {
		return documents.stream().map(DocumentMapper::toDto)
				.sorted(Comparator.comparing(DocumentResponseDto::effectiveDate).reversed()).toList();
	}

	private List<CollaboratorResponseDto> MapCollaboratorsToDto(List<Collaborator> collaborators) {
		return collaborators.stream().map(CollaboratorMapper::toDto)
				.sorted(Comparator.comparing(CollaboratorResponseDto::priority)).toList();
	}

	private String buildLinkMessage(int docs, int collabs) {
		var msg = new StringBuilder("Não é possível excluir a categoria porque existem vínculos:");

		if (docs > 0)
			msg.append(" ").append(docs).append(" documento(s)");

		if (collabs > 0)
			msg.append(docs > 0 ? " e " : " ").append(collabs).append(" colaborador(es)");

		return msg.toString();
	}
}