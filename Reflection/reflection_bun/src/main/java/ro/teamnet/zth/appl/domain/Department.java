package ro.teamnet.zth.appl.domain;

import ro.teamnet.zth.api.annotations.Column;
import ro.teamnet.zth.api.annotations.Id;

/**
 * Created by user on 7/7/2016.
 */
public class Department {
    @Id(name = "department_id")
    private long id;
    @Column(name = "department_name")
    private String departmentName;
    @Column(name = "location_id")
    private Location locationId;

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", departmentName='" + departmentName + '\'' +
                ", location=" + locationId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;

        Department that = (Department) o;

        if (getId() != that.getId()) return false;
        if (getDepartmentName() != null ? !getDepartmentName().equals(that.getDepartmentName()) : that.getDepartmentName() != null)
            return false;
        return getLocationId() != null ? getLocationId().equals(that.getLocationId()) : that.getLocationId() == null;

    }

    @Override
    public int hashCode() {
        return 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Location getLocationId() {
        return locationId;
    }

    public void setLocationId(Location location) {
        this.locationId = locationId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
