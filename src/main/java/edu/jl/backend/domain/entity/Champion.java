package edu.jl.backend.domain.entity;

import java.util.Objects;

public final class Champion {
    private Long id;
    private String name;
    private String title;
    private String lore;
    private String imageUrl;

    public Champion(Long id, String name, String title, String lore, String imageUrl) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.lore = lore;
        this.imageUrl = imageUrl;
    }

    public Champion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Champion champion = (Champion) object;
        return Objects.equals(id, champion.id) && Objects.equals(name, champion.name) && Objects.equals(title, champion.title) && Objects.equals(lore, champion.lore) && Objects.equals(imageUrl, champion.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, lore, imageUrl);
    }
}
