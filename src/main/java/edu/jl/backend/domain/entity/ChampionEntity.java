package edu.jl.backend.domain.entity;

import java.util.Objects;

public final class ChampionEntity {
    private Long id;
    private String name;
    private String title;
    private String lore;
    private String imageUrl;

    public ChampionEntity(Long id, String name, String title, String lore, String imageUrl) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.lore = lore;
        this.imageUrl = imageUrl;
    }

    public ChampionEntity(String name, String title, String lore, String imageUrl) {
        this.name = name;
        this.title = title;
        this.lore = lore;
        this.imageUrl = imageUrl;
    }

    public ChampionEntity() {
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
        ChampionEntity that = (ChampionEntity) object;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(title, that.title) && Objects.equals(lore, that.lore) && Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, lore, imageUrl);
    }
}
