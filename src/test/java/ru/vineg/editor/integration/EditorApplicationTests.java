package ru.vineg.editor.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.vineg.editor.entity.Insertion;
import ru.vineg.editor.dto.CreateEntityResponseDto;
import ru.vineg.editor.service.EditorSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EditorApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private EditorSession editorSession;

	private ObjectMapper om = new ObjectMapper();

	@Test
	public void contextLoads() {
	}

	@Test
	public void creationWorks() throws Exception {
		String createdId = createDocument();
		Assert.assertNotEquals(null, createdId);
	}

	@Test
	public void editionWorks() throws Exception {
		String id = createDocument();
		Insertion insertion = new Insertion(null, "test", editorSession);
		MockHttpServletResponse response = this.mockMvc.perform(post("/document/" + id + "/edits?fromRevision=0")
				.content(om.writeValueAsString(insertion))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse();
	}

	private String createDocument() throws Exception {
		MockHttpServletResponse response = this.mockMvc.perform(post("/document/create")).andDo(print()).andExpect(status().isOk()).andReturn().getResponse();
		return om.readValue(response.getContentAsString(), CreateEntityResponseDto.class).getCreatedId();
	}
}
