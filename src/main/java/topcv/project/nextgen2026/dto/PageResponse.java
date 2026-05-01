package topcv.project.nextgen2026.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    
    private List<T> data;
    
    private int page;
    
    private int size;
    
    private long totalElements;
    
    private int totalPages;
    
    private boolean isFirst;
    
    private boolean isLast;
    
    private boolean hasNext;
    
    private boolean hasPrevious;
}
