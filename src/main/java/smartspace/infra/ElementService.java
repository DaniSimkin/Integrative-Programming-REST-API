package smartspace.infra;

import java.util.List;

import smartspace.data.ElementEntity;
import smartspace.layout.ElementBoundary;

public interface ElementService {
	
	public List<ElementEntity> newElement(ElementEntity[] allBoundaries, String adminSmartspace, String adminEmail);

	public List<ElementEntity> getUsingPagination(
			int size, int page, String adminSmartspace, String adminEmail);
}
