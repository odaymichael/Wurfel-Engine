object CreateMap: TCreateMap
  Left = 305
  Top = 640
  Width = 503
  Height = 240
  BorderIcons = [biSystemMenu, biMinimize]
  Caption = 'Create Map'
  Color = clBtnFace
  Constraints.MinHeight = 190
  Constraints.MinWidth = 270
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  Icon.Data = {
    0000010001002020100000000000E80200001600000028000000200000004000
    0000010004000000000080020000000000000000000000000000000000000000
    000000008000008000000080800080000000800080008080000080808000C0C0
    C0000000FF0000FF000000FFFF00FF000000FF00FF00FFFF0000FFFFFF000000
    0000000000000000000000000000000000000000000000000000000000000000
    0000000000000000000000000000000000000000000000000000000000000000
    0000000000000000000000000000000000000000000000000000000000000000
    0000000000000000000000000000000000000000000000000000000000000000
    0000000000022000000000000000000000000000022222200000000000000000
    0000000222222222200000000000000000000222222222222220000000000000
    0002222222222222222220000000000002222222222222222222222000000002
    2222222222222222222222222000022222222222222222222222222222200002
    2222222222222222222222222000000002222222222222222222222000000000
    0002222222222222222220000000000000000222222222222220000000000000
    0000000222222222200000000000000000000000022222200000000000000000
    0000000000022000000000000000000000000000000000000000000000000000
    0000000000000000000000000000000000000000000000000000000000000000
    0000000000000000000000000000000000000000000000000000000000000000
    0000000000000000000000000000000000000000000000000000000000000000
    000000000000000000000000000000000000000000000000000000000000FFFF
    FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFE7FFFFFF8
    1FFFFFE007FFFF8001FFFE00007FF800001FE000000780000001000000008000
    0001E0000007F800001FFE00007FFF8001FFFFE007FFFFF81FFFFFFE7FFFFFFF
    FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF}
  OldCreateOrder = False
  Position = poScreenCenter
  OnCreate = FormCreate
  DesignSize = (
    487
    205)
  PixelsPerInch = 96
  TextHeight = 13
  object btPath: TButton
    Left = 0
    Top = 80
    Width = 91
    Height = 21
    Caption = 'Select path'
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'Verdana'
    Font.Style = [fsBold]
    ParentFont = False
    TabOrder = 1
    OnClick = btPathClick
  end
  object leMapname: TLabeledEdit
    Left = 0
    Top = 30
    Width = 184
    Height = 24
    Anchors = [akLeft, akTop, akRight]
    EditLabel.Width = 78
    EditLabel.Height = 18
    EditLabel.Caption = 'Mapname'
    EditLabel.Font.Charset = ANSI_CHARSET
    EditLabel.Font.Color = clWindowText
    EditLabel.Font.Height = -16
    EditLabel.Font.Name = 'Verdana'
    EditLabel.Font.Style = []
    EditLabel.ParentFont = False
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -13
    Font.Name = 'Verdana'
    Font.Style = []
    LabelSpacing = 5
    MaxLength = 100
    ParentFont = False
    TabOrder = 0
    Text = 'Fill mapname here'
    OnChange = leMapnameChange
  end
  object lePath: TLabeledEdit
    Left = 100
    Top = 80
    Width = 381
    Height = 23
    EditLabel.Width = 32
    EditLabel.Height = 15
    EditLabel.Caption = 'lePath'
    EditLabel.Font.Charset = ANSI_CHARSET
    EditLabel.Font.Color = clWindowText
    EditLabel.Font.Height = -11
    EditLabel.Font.Name = 'Lucida Sans Unicode'
    EditLabel.Font.Style = []
    EditLabel.ParentFont = False
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'Lucida Sans Unicode'
    Font.Style = []
    ParentFont = False
    TabOrder = 2
  end
  object leChunksizeX: TLabeledEdit
    Left = 0
    Top = 130
    Width = 61
    Height = 23
    EditLabel.Width = 63
    EditLabel.Height = 15
    EditLabel.Caption = 'ChunksizeX'
    EditLabel.Font.Charset = ANSI_CHARSET
    EditLabel.Font.Color = clWindowText
    EditLabel.Font.Height = -11
    EditLabel.Font.Name = 'Lucida Sans Unicode'
    EditLabel.Font.Style = []
    EditLabel.ParentFont = False
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'Lucida Sans Unicode'
    Font.Style = []
    ParentFont = False
    TabOrder = 3
    Text = '12'
  end
  object leChunksizeY: TLabeledEdit
    Left = 70
    Top = 130
    Width = 61
    Height = 23
    EditLabel.Width = 63
    EditLabel.Height = 15
    EditLabel.Caption = 'ChunksizeY'
    EditLabel.Font.Charset = ANSI_CHARSET
    EditLabel.Font.Color = clWindowText
    EditLabel.Font.Height = -11
    EditLabel.Font.Name = 'Lucida Sans Unicode'
    EditLabel.Font.Style = []
    EditLabel.ParentFont = False
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'Lucida Sans Unicode'
    Font.Style = []
    ParentFont = False
    TabOrder = 4
    Text = '30'
  end
  object leChunksizeZ: TLabeledEdit
    Left = 140
    Top = 130
    Width = 61
    Height = 23
    EditLabel.Width = 63
    EditLabel.Height = 15
    EditLabel.Caption = 'ChunksizeZ'
    EditLabel.Font.Charset = ANSI_CHARSET
    EditLabel.Font.Color = clWindowText
    EditLabel.Font.Height = -11
    EditLabel.Font.Name = 'Lucida Sans Unicode'
    EditLabel.Font.Style = []
    EditLabel.ParentFont = False
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'Lucida Sans Unicode'
    Font.Style = []
    ParentFont = False
    TabOrder = 5
    Text = '10'
  end
  object bbOK: TBitBtn
    Left = 410
    Top = 170
    Width = 75
    Height = 25
    TabOrder = 6
    OnClick = bbOKClick
    Kind = bkOK
  end
  object bbClose: TBitBtn
    Left = 320
    Top = 170
    Width = 81
    Height = 25
    TabOrder = 7
    Kind = bkCancel
  end
end
