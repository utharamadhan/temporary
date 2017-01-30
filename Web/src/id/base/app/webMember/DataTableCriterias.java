package id.base.app.webMember;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DataTableCriterias implements Serializable{

	private static final long serialVersionUID = 1181658699996864787L;

	private int draw;
    private int start;
    private int length;

    private Map<SearchCriterias, String> search;

    private List<Map<ColumnCriterias, String>> columns;

    private List<Map<OrderCriterias, String>> order;
    
    private Map<String, String> customFilters;

    public enum SearchCriterias {
        value,
        regex
    }
    public enum OrderCriterias {
        column,
        dir
    }
    public enum ColumnCriterias {
        data,
        name,
        searchable,
        orderable,
        searchValue,
        searchRegex
    }
	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public Map<SearchCriterias, String> getSearch() {
		return search;
	}
	public void setSearch(Map<SearchCriterias, String> search) {
		this.search = search;
	}
	public List<Map<ColumnCriterias, String>> getColumns() {
		return columns;
	}
	public void setColumns(List<Map<ColumnCriterias, String>> columns) {
		this.columns = columns;
	}
	public List<Map<OrderCriterias, String>> getOrder() {
		return order;
	}
	public void setOrder(List<Map<OrderCriterias, String>> order) {
		this.order = order;
	}
	public Map<String, String> getCustomFilters() {
		return customFilters;
	}
	public void setCustomFilters(Map<String, String> customFilters) {
		this.customFilters = customFilters;
	}
}