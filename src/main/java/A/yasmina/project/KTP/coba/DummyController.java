/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package A.yasmina.project.KTP.coba;

import java.awt.Image;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.text.SimpleDateFormat;
import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ACHI
 */
@Controller
public class DummyController {
    DummyJpaController dummyController = new DummyJpaController();
    List<Dummy> data = new ArrayList<>();
    
    @RequestMapping("/read")
    //@ResponseBody
    public String getDummy (Model model) {
    int record = dummyController.getDummyCount();
        String result = "";
        try{
            data = dummyController.findDummyEntities().subList(0, record);
        }
        catch (Exception e){
            result=e.getMessage();
        }
        
        model.addAttribute("godummy", data);
         model.addAttribute("record", record);
         
        return "dummy";    
    }
    
    @RequestMapping("/create")
    public String createDummy(){
        return "dummy/create";
    }
    
     @PostMapping(value="/newdata", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public String newDummy(HttpServletRequest data,@RequestParam("gambar") MultipartFile file) throws ParseException, Exception{
        Dummy dumdata = new Dummy();
        
        String id = data.getParameter("id");
        int iid = Integer.parseInt(id);
        String tanggal = data.getParameter("tanggal");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
        byte[] image = file.getBytes();
        dumdata.setId(iid);
        dumdata.setTanggal(date);
        dumdata.setGambar(image);
        dummyController.create(dumdata);
        
        return "redirect:/read";
    }
    
    @RequestMapping (value="/gambar" , method = RequestMethod.GET ,produces = {MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getImg(@RequestParam("id") int id) throws Exception {
	Dummy dum = dummyController.findDummy(id);
	byte[] gambar = dum.getGambar();
	return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(gambar);
    }
    
    @GetMapping("/delete/{id}")
   public String deletedummy (@PathVariable("id") int id) throws Exception{
       dummyController.destroy(id);
       return "redirect:/read";
   }
    
//    @DeleteMapping("/sepatu/{id}")
//    public ResponseEntity<HttpStatus> deleteSepatuById(@PathVariable Long id){
//        sepatu.deleteById(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
   @GetMapping("/edit/{id}")
    public String editDummy(Model model, @PathVariable("id") int id) throws Exception{
        Dummy dum = dummyController.findDummy(id);
        model.addAttribute("godummy", dum);
        return "dummy/edit";
    }
     @PostMapping(value="/newedit", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
     public String updateDummy(HttpServletRequest data,@RequestParam("gambar") MultipartFile file) throws ParseException, Exception{
        Dummy dum = new Dummy();
        
        String id = data.getParameter("id");
        int iid = Integer.parseInt(id);
        String tanggal = data.getParameter("tanggal");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(data.getParameter("tanggal"));
        byte[] image = file.getBytes();
        dum.setId(iid);
        dum.setTanggal(date);
        dum.setGambar(image);
        dummyController.edit(dum);
        
        return "redirect:/read";
    }
     
//    @RequestMapping("/filename")
//    public Image getImageDetails(HttpServletRequest data ,@RequestParam("gambar") MultipartFile multipartFile) throws Exception{
//        Dummy dum = new Dummy();
//        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//        byte[] image = multipartFile.getBytes();
//        return "dummy/read";
//    }
     
}
