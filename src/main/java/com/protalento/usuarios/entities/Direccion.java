package com.protalento.usuarios.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Direccion {
	private String calle;
	private String ciudad;
	private String codigo_postal;
	private Geolocalizacion geolocalizacion;
}
