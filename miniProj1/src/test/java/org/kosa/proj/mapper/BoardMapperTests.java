package org.kosa.proj.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kosa.proj.board.BoardMapper;
import org.kosa.proj.entity.BoardVO;
import org.kosa.proj.page.PageRequestVO;
import org.kosa.proj.page.PageResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations="file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class BoardMapperTests {

    @Autowired
    private BoardMapper boardMapper;
    
    @Test
    @DisplayName("null 객체 여부 확인 ")
    public void null_객체_여부_확인() {
    	//given(준비): 어떠한 데이터가 준비되었을 때
    	BoardVO boardVO = BoardVO.builder().bno("0").build();
    	
    	//when(실행): 어떠한 함수를 실행하면
    	BoardVO resultVO = boardMapper.view(boardVO);
    	
    	//then(검증): 어떠한 결과가 나와야 한다.
    	assertNull(resultVO);
    }
    
    @Test
    @DisplayName("incCount 값증가 확인")
    public void incCount_값증가_확인() {
    	//given(준비): 어떠한 데이터가 준비되었을 때
    	BoardVO boardVO = BoardVO.builder().bno("1005").build();
    	
    	//when(실행): 어떠한 함수를 실행하면
    	BoardVO beforeVO = boardMapper.view(boardVO);
    	boardMapper.incViewCount(boardVO);
    	BoardVO resultVO = boardMapper.view(boardVO);
    	
    	//then(검증): 어떠한 결과가 나와야 한다.
    	assertEquals(Integer.parseInt(beforeVO.getView_count()) + 1,  Integer.parseInt(resultVO.getView_count()));
    }

    @Test
    public void testClearAll() {
    	boardMapper.clear();
    }

    @Test
    public void testCreateSampleData() {
    	for (int i=0;i<200;i++) {
	    	BoardVO boardVO = BoardVO.builder()
	    			.btitle("게시물 제목 " + i)
	    			.bcontent("게시물 내용 " + i)
	    			.build();
	    	
	    	if (i % 2 == 0) {
	    		boardVO.setMemberID("msa1");
	    	} else {
	    		boardVO.setMemberID("msa2");
	    	}
	    	
	    	boardMapper.insert(boardVO);
    	}
    }
    
    @Test 
    public void testPage() {
        PageRequestVO  pageRequestVO = PageRequestVO.builder().pageNo(1).size(10).build();
    	List<BoardVO> list = boardMapper.getList(pageRequestVO);
        int total = boardMapper.getTotalCount(pageRequestVO);
        
        log.info("list {} ", list);
        log.info("total  = {} ", total);

        PageResponseVO<BoardVO> pageResponseVO = PageResponseVO.<BoardVO>withAll()
                .list(list)
                .total(total)
                .size(pageRequestVO.getSize())
                .pageNo(pageRequestVO.getPageNo())
                .build();

        log.info("pageResponseVO  = {} ", pageResponseVO);

    }
}
