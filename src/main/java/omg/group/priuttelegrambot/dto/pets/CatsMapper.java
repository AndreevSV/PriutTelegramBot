package omg.group.priuttelegrambot.dto.pets;

import omg.group.priuttelegrambot.entity.pets.Cat;

public class CatsMapper {
    public static CatDto toDto(Cat pet) {
        CatDto dto = new CatDto();
        dto.setId(pet.getId());
        dto.setAnimalType(pet.getAnimalType());
        dto.setSex(pet.getSex());
        dto.setNickName(pet.getNickName());
        dto.setBirthday(pet.getBirthday());
        dto.setBreed(pet.getBreed());
        dto.setDisabilities(pet.getDisabilities());
        dto.setDescription(pet.getDescription());
        dto.setCreatedAt(pet.getCreatedAt());
        dto.setUpdatedAt(pet.getUpdatedAt());
        dto.setDateOutcome(pet.getDateOutcome());
        dto.setPhotoPath(pet.getPhotoPath());
        dto.setVolunteer(pet.getVolunteer());
        dto.setOwner(pet.getOwner());
        dto.setFirstProbation(pet.getFirstProbation());
        dto.setSecondProbation(pet.getSecondProbation());
        dto.setProbationStarts(pet.getProbationStarts());
        dto.setProbationEnds(pet.getProbationEnds());
        dto.setPassedFirstProbation(pet.getPassedFirstProbation());
        dto.setPassedSecondProbation(pet.getPassedSecondProbation());

        return dto;
    }

    public static Cat toEntity(CatDto dto) {
        Cat pet = new Cat();
        pet.setId(dto.getId());
        pet.setAnimalType(dto.getAnimalType());
        pet.setSex(dto.getSex());
        pet.setNickName(dto.getNickName());
        pet.setBirthday(dto.getBirthday());
        pet.setBreed(dto.getBreed());
        pet.setDisabilities(dto.getDisabilities());
        pet.setDescription(dto.getDescription());
        pet.setCreatedAt(dto.getCreatedAt());
        pet.setUpdatedAt(dto.getUpdatedAt());
        pet.setDateOutcome(dto.getDateOutcome());
        pet.setPhotoPath(dto.getPhotoPath());
        pet.setVolunteer(dto.getVolunteer());
        pet.setOwner(dto.getOwner());
        pet.setFirstProbation(dto.getFirstProbation());
        pet.setSecondProbation(dto.getSecondProbation());
        pet.setProbationStarts(dto.getProbationStarts());
        pet.setProbationEnds(dto.getProbationEnds());
        pet.setPassedFirstProbation(dto.getPassedFirstProbation());
        pet.setPassedSecondProbation(dto.getPassedSecondProbation());
        return pet;
    }
}




