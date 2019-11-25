package co.ariskycode.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Greet implements Serializable {
	
	private static final long serialVersionUID = -4590891596001783840L;
	
	private String message;
	
}
