package br.intsys.studentjwt.dto;

import br.intsys.studentjwt.anotations.PerfilFK;

public class UsuarioperfilDTO {
    @PerfilFK
    private Integer perfil_id;

    public Integer getPerfil_id() {
            return perfil_id;
    }
}