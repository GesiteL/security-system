package tftc.lch.security.presentation.dto.response;

import java.util.List;

/** 分页响应包装类（泛型）。 */
public class PageResponse<T> {
    private List<T> items;
    private int total;
    private int page;
    private int pageSize;

    public static <T> PageResponse<T> of(List<T> items, int total, int page, int pageSize) {
        PageResponse<T> r = new PageResponse<>();
        r.items = items;
        r.total = total;
        r.page = page;
        r.pageSize = pageSize;
        return r;
    }

    public List<T> getItems() { return items; }
    public int getTotal() { return total; }
    public int getPage() { return page; }
    public int getPageSize() { return pageSize; }
}
